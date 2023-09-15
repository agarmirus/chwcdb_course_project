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
	id int primary key,
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
	condition text check (condition = ''),
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

create or replace function is_achieved(condition text, result int) returns boolean as
$$
	begin
		if result == 0 then
			return	condition == 'X' or condition == '1X' or
					condition == '2X' or condition == 'X1' or
					condition == 'X2';
		elsif result == 1 then
			return 	condition == '1' or condition == '1X' or
					condition == 'X1';
		elsif result == 2 then
			return 	condition == '2' or condition == '2X' or
					condition == 'X2';
		end if;
	end;
$$
language plpgsql;

create or replace function is_main_line(condition text) returns boolean as
$$
	begin
		return 	condition == '1' or condition == '2' or
				condition == 'X' or condition == '1X' or
				condition == '2X' or condition == 'X1' or
				condition == 'X2';
	end;
$$
language plpgsql;

create or replace function new_express_status(bet record, game record) returns int as 
$$
	declare 
		express_status int;
		encl_bet record;
	begin
		express_status = 1;
		for encl_bet in
			select enclosure_id as id, type, condition, coefficient, status, game_id
			from game_moves gm join bets b on enclosure_id = id
			where bet_id = bet.id
		loop
			if encl_bet.game_id == game.id then
				if is_main_line(encl_bet.condition) then
					if is_achieved(encl_bet.condition, game.result) then
						update bets set status = 1 where id = encl_bet.id;
					else
						update bets set status = 2 where id = encl_bet.id;
						express_status = 2;
					end if;
				elsif express_status != 2 and encl_bet == 0 then
					express_status = 0;
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
			if bet.type == 0 then
				if bet.game_id == new.id then
					if is_main_line(bet.condition) then
						if is_achieved(bet.condition, new.result) then
							update bets set status = 1 where id = bet.id;
						else
							update bets set status = 2 where id = bet.id;
						end if;
					end if;
				end if;
			
			elsif bet.type == 1 then
				express_status = new_express_status(bet, game);
				if express_status != bet.status then
					update bets set status = express_status where id = bet.id;
				end if;
		
			elsif bet.type == 2 then
				for express in
					select enclosure_id as id, type, condition, coefficient, status, game_id
					from game_moves gm join bets b on enclosure_id = id
					where bet_id = bet.id
				loop
					express_status = new_express_status(express, new);
					if express_status != express.status then
						update bets set status = express_status where id = express.id;
					end if;
				end loop;
			end if;
		end loop;
		return new;
	end;
$update_bets_status_trigger$
language plpgsql;

create or replace function remove_next_moves() returns trigger as
$remove_next_moves_trigger$
	begin
    	
	end;
$remove_next_moves_trigger$
language plpgsql;
