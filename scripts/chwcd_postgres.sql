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
	referee_id int references referees (id),
	first_player_id int references players (id),
	second_player_id int references players (id)
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
	game_id int references games (id)
);

create table if not exists game_moves
(
	game_id int references games (id),
	move_id int references moves (id),
	
	comment text
);

create table if not exists bet_enclosures
(
	bet_id int references bets (id),
	enclosure_id int references bets (id)
);

alter table bet_enclosures add constraint bet_id check (bet_id != enclosure_id);
alter table bet_enclosures add constraint enclosure_id check (bet_id != enclosure_id);

create or replace function is_achieved(bet record, game record) returns boolean as
$$
	begin
		return true;
	end;
$$
language plpgsql;

create or replace function update_bets_status() returns trigger as 
$update_bets_status_trigger$
	declare
		bet record;
	begin 
		for bet in select * from bets
		loop
			if bet.type == 0 then
				if bet.game_id == new.id and bet.status = 0 then
					if is_achieved(bet, new) then
						update bets set status = 1 where id = bet.id;
					else
						update bets set status = 0 where id = bet.id;
					end if;
				end if;
			elsif 
--				...
			end if;
		end loop;
	end;
$update_bets_status_trigger$
language plpgsql;
