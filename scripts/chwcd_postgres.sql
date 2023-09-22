create table if not exists users
(
	login text primary key,
	hashed_pswd text not null check (hashed_pswd != ''),
	role int not null check (role <= 2 and role >= 0)
);

create table if not exists referees
(
	id int primary key,
	first_name text not null check (first_name != ''),
	second_name text not null check (second_name != ''),
	third_name text,
	birth_date date not null,
	country text not null check (country != '')
);

create table if not exists players
(
	id int primary key,
	first_name text not null check (first_name != ''),
	second_name text not null check (second_name != ''),
	third_name text,
	birth_date date not null,
	country text not null check (country != ''),
	raiting int not null check (raiting >= 0)
);

create table if not exists moves
(
	id serial primary key,
	figure int not null check (figure >= 0 and figure <= 5),
	start_cell text not null check (start_cell != ''),
	end_cell text not null check (end_cell != '')
);

create table if not exists games
(
	id int primary key,
	round int not null check (round >= 1 and round <= 8),
	duration int,
	number int not null check (number > 0),
	result int check (result >= 0 and result <= 2),
	date date,
	referee_id int references referees (id) on delete cascade,
	first_player_id int references players (id) on delete cascade,
	second_player_id int references players (id) on delete cascade
);

alter table games add constraint first_player_id check (first_player_id != second_player_id);
alter table games add constraint second_player_id check (first_player_id != second_player_id);

create table if not exists bets
(
	id int primary key,
	type int not null check (type >= 0 and type <= 2),
	condition text check (condition != ''),
	coefficient real check (coefficient > 1),
	status int not null check (status >= 0 and status <= 2),
	game_id int references games (id) on delete cascade
);

create table if not exists game_moves
(
	game_id int references games (id) on delete cascade,
	move_id int references moves (id) on delete cascade,
	
	number int not null check (number > 0),
	
	comment text
);

create table if not exists bet_enclosures
(
	bet_id int references bets (id) on delete cascade,
	enclosure_id int references bets (id) on delete cascade
);

alter table bet_enclosures add constraint bet_id check (bet_id != enclosure_id);
alter table bet_enclosures add constraint enclosure_id check (bet_id != enclosure_id);

create user unauthorized with encrypted password 'unauthorized';
grant select on users to unauthorized;

create user administrator with encrypted password 'administrator';
grant select, update, delete, insert on bet_enclosures to administrator;
grant select, update, delete, insert on bets to administrator;
grant select, update, delete, insert on bet_enclosures to administrator;
grant select, update, delete, insert on game_moves to administrator;
grant select, update, delete, insert on games to administrator;
grant select, update, delete, insert on moves to administrator;
grant select, update, delete, insert on players to administrator;
grant select, update, delete, insert on referees to administrator;
grant select, update, delete, insert on users to administrator;

create user spectator with encrypted password 'spectator';
grant select on referees, players to spectator;
grant select, update, delete, insert on games to spectator;
grant select, update, delete, insert on moves to spectator;
grant select, update, delete, insert on game_moves to spectator;
grant select, update on bets to spectator;
grant select on bet_enclosures to spectator;
grant update on players to spectator;
grant usage, select on sequence moves_id_seq to spectator;

create user bookmaker with encrypted password 'bookmaker';
grant select on referees, players, games, moves, game_moves to bookmaker;
grant select, update, delete, insert on bets to bookmaker;
grant select, update, delete, insert on bet_enclosures to bookmaker;

create or replace function is_achieved(condition text, result int) returns boolean as
$$
	begin
		if result = 0 then
			return	condition = 'X' or condition = '1X' or
					condition = '2X' or condition = 'X1' or
					condition = 'X2';
		elsif result = 1 then
			return 	condition = '1' or condition = '1X' or
					condition = 'X1';
		elsif result = 2 then
			return 	condition = '2' or condition = '2X' or
					condition = 'X2';
		end if;
		return false;
	end;
$$
language plpgsql;

create or replace function is_main_line(condition text) returns boolean as
$$
	begin
		return 	condition = '1' or condition = '2' or
				condition = 'X' or condition = '1X' or
				condition = '2X' or condition = 'X1' or
				condition = 'X2';
	end;
$$
language plpgsql;

create or replace function new_express_status(bet record, game record) returns int as 
$$
	declare 
		express_status int;
		encl_bet record;
	begin
		express_status := 1;
		for encl_bet in
			select enclosure_id as id, type, condition, coefficient, status, game_id
			from bet_enclosures be join bets b on enclosure_id = id
			where bet_id = bet.id
		loop
			if encl_bet.game_id = game.id then
				if is_main_line(encl_bet.condition) then
					if is_achieved(encl_bet.condition, game.result) then
						update bets set status = 1 where id = encl_bet.id;
					else
						update bets set status = 2 where id = encl_bet.id;
						express_status := 2;
					end if;
				elsif express_status != 2 and encl_bet = 0 then
					express_status := 0;
				end if;
			end if;
		end loop;
		return express_status;
	end;
$$
language plpgsql;

create or replace function update_bets_status() returns trigger as 
$update_bets_status_trigger$
	declare
		bet record;
		express_status int;
		express record;
	begin 
		for bet in select * from bets
		loop
			if bet.type = 0 then
				if bet.game_id = old.id then
					if is_main_line(bet.condition) then
						if is_achieved(bet.condition, old.result) then
							update bets set status = 1 where id = bet.id;
						else
							update bets set status = 2 where id = bet.id;
						end if;
					end if;
				end if;
			
			elsif bet.type = 1 then
				express_status := new_express_status(bet, old);
				if express_status != bet.status then
					update bets set status = express_status where id = bet.id;
				end if;
		
			elsif bet.type = 2 then
				for express in
					select enclosure_id as id, type, condition, coefficient, status, game_id
					from bet_enclosures be join bets b on enclosure_id = id
					where bet_id = bet.id
				loop
					express_status := new_express_status(express, old);
					if express_status != express.status then
						update bets set status = express_status where id = express.id;
					end if;
				end loop;
			end if;
		end loop;
		return null;
	end;
$update_bets_status_trigger$
language plpgsql;

update games set duration = 320, result = 1 where id = 1;

create trigger update_bets_status_trigger
after update of result on games
for each row
execute function update_bets_status();

create or replace function remove_next_moves() returns trigger as
$remove_next_moves_trigger$
	begin
    	delete from game_moves where move_id >= old.move_id and game_id = old.game_id;
        delete from moves where id not in (select move_id from game_moves);
        return null;
	end;
$remove_next_moves_trigger$
language plpgsql;

create trigger remove_next_moves_trigger
after delete on game_moves
for each row
when (pg_trigger_depth() = 0)
execute function remove_next_moves();

create function weak_player_win_prob(diff int) returns real as 
$$
    begin
        if diff >= 0 and diff <= 3 then
            return 0.5;
        elsif diff >= 4 and diff <= 10 then
            return 0.49;
        elsif diff >= 11 and diff <= 17 then
            return 0.48;
        elsif diff >= 18 and diff <= 25 then
            return 0.47;
        elsif diff >= 26 and diff <= 32 then
            return 0.46;
        elsif diff >= 33 and diff <= 39 then
            return 0.45;
        elsif diff >= 40 and diff <= 46 then
            return 0.44;
        elsif diff >= 47 and diff <= 53 then
            return 0.43;
        elsif diff >= 54 and diff <= 61 then
            return 0.42;
        elsif diff >= 62 and diff <= 68 then
            return 0.41;
        elsif diff >= 69 and diff <= 76 then
            return 0.4;
        elsif diff >= 77 and diff <= 83 then
            return 0.39;
        elsif diff >= 84 and diff <= 91 then
            return 0.38;
        elsif diff >= 92 and diff <= 98 then
            return 0.37;
        elsif diff >= 99 and diff <= 106 then
            return 0.36;
        elsif diff >= 107 and diff <= 113 then
            return 0.35;
        elsif diff >= 114 and diff <= 121 then
            return 0.34;
        elsif diff >= 122 and diff <= 129 then
            return 0.33;
        elsif diff >= 130 and diff <= 137 then
            return 0.32;
        elsif diff >= 138 and diff <= 145 then
            return 0.31;
        elsif diff >= 146 and diff <= 153 then
            return 0.3;
        elsif diff >= 154 and diff <= 162 then
            return 0.29;
        elsif diff >= 163 and diff <= 170 then
            return 0.28;
        elsif diff >= 171 and diff <= 179 then
            return 0.27;
        elsif diff >= 180 and diff <= 188 then
            return 0.26;
        elsif diff >= 189 and diff <= 197 then
            return 0.25;
        elsif diff >= 198 and diff <= 206 then
            return 0.24;
        elsif diff >= 207 and diff <= 215 then
            return 0.23;
        elsif diff >= 216 and diff <= 225 then
            return 0.22;
        elsif diff >= 226 and diff <= 235 then
            return 0.21;
        elsif diff >= 236 and diff <= 245 then
            return 0.2;
        elsif diff >= 246 and diff <= 256 then
            return 0.19;
        elsif diff >= 257 and diff <= 267 then
            return 0.18;
        elsif diff >= 268 and diff <= 278 then
            return 0.17;
        elsif diff >= 279 and diff <= 290 then
            return 0.16;
        elsif diff >= 291 and diff <= 302 then
            return 0.15;
        elsif diff >= 303 and diff <= 315 then
            return 0.14;
        elsif diff >= 316 and diff <= 328 then
            return 0.13;
        elsif diff >= 329 and diff <= 344 then
            return 0.12;
        elsif diff >= 345 and diff <= 357 then
            return 0.11;
        elsif diff >= 358 and diff <= 374 then
            return 0.1;
        elsif diff >= 375 and diff <= 391 then
            return 0.09;
        elsif diff >= 392 and diff <= 411 then
            return 0.08;
        elsif diff >= 412 and diff <= 432 then
            return 0.07;
        elsif diff >= 433 and diff <= 456 then
            return 0.06;
        elsif diff >= 457 and diff <= 484 then
            return 0.05;
        elsif diff >= 485 and diff <= 517 then
            return 0.04;
        elsif diff >= 518 and diff <= 559 then
            return 0.03;
        elsif diff >= 560 and diff <= 619 then
            return 0.02;
        elsif diff >= 620 and diff <= 735 then
            return 0.01;
        end if;
        return 0.0;
    end;
$$
language plpgsql;

create or replace function update_raitings() returns trigger as
$update_raitings_trigger$
    declare
        prob1 real;
        prob2 real;
        raiting1 int;
        raiting2 int;
        new_raiting1 int;
        new_raiting2 int;
        delta1 int;
        delta2 int;
        raiting_delta1 int;
        raiting_delta2 int;
    begin
    	select raiting into raiting1 from players where id = new.first_player_id;
        select raiting into raiting2 from players where id = new.second_player_id;
        
        if (raiting1 > raiting2) then
            select weak_player_win_prob(raiting1 - raiting2) into prob2;
            prob1 := 1.0 - prob2;
        else
            select weak_player_win_prob(raiting2 - raiting1) into prob1;
            prob2 := 1.0 - prob1;
        end if;
    
        if new.result = 0 then
            delta1 := 0.5 - prob1;
            delta2 := 0.5 - prob2;
        elsif new.result = 1 then
            delta1 := 1 - prob1;
            delta2 := -1 * prob2;
        else
            delta1 := -1 * prob1;
            delta2 := 1 - prob2;
        end if;
    
        if raiting1 <= 2400 then
            raiting_delta1 := delta1 * 20;
        else
            raiting_delta1 := delta1 * 10;
        end if;
        if raiting2 <= 2400 then
            raiting_delta2 := delta2 * 20;
        else
            raiting_delta2 := delta2 * 10;
        end if;
        if raiting_delta1 > 700 then
            raiting_delta1 := 700;
        end if;
        if raiting_delta2 > 700 then
            raiting_delta2 := 700;
        end if;
        update players set raiting = raiting + raiting_delta1 where id = new.first_player_id;
        update players set raiting = raiting + raiting_delta2 where id = new.second_player_id;
    
        return new;
    end;
$update_raitings_trigger$
language plpgsql;

create trigger update_raitings_trigger
after update of result on games
for each row 
execute function update_raitings();

select game_id, round, duration, g.number as number, result, date, referee_id, first_player_id, second_player_id, move_id, jm.number as move_number, figure, start_cell, end_cell, comment
from
(
    select game_id, move_id, figure, start_cell, end_cell, number, comment
    from game_moves gm join moves m on move_id = m.id
) as jm
join games g on g.id = game_id
where game_id = 1
order by move_number;

do
$$
    begin
        for i in 3..100
        loop
            insert into games values (i, 1, 120, 1, 0, '2001-12-12', 1, 1, 2);
        end loop;
    end;
$$
