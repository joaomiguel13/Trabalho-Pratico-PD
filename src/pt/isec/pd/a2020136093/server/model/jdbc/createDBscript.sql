--
-- File generated with SQLiteStudio
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Criar tabela de versão da base de dados
CREATE TABLE IF NOT EXISTS db_version (version INTEGER NOT NULL UNIQUE, PRIMARY KEY (version));

-- Table: accounts
CREATE TABLE IF NOT EXISTS accounts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name TEXT NOT NULL,email TEXT NOT NULL,password TEXT NOT NULL,admin BOOLEAN NOT NULL DEFAULT (false),nIdentificacao INTEGER NOT NULL);

-- IGNORE faz com que seja criada a conta apenas se o id não existir evitando assim erros
INSERT OR IGNORE INTO accounts (id, name, email, password, admin, nIdentificacao) VALUES (true, 'admin', 'admin', 'admin', true, 0);

-- Table: events
CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name TEXT NOT NULL,code TEXT,local TEXT NOT NULL,date TEXT NOT NULL,timeStart TEXT NOT NULL,timeEnd TEXT NOT NULL,nPresences INTEGER NOT NULL DEFAULT (0));

-- Table: accounts_events
CREATE TABLE IF NOT EXISTS accounts_events (account_id INTEGER NOT NULL,event_id INTEGER NOT NULL,PRIMARY KEY (account_id, event_id),FOREIGN KEY (account_id) REFERENCES accounts (id),FOREIGN KEY (event_id) REFERENCES events (id));

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;