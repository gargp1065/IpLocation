#!/bin/bash
conffile=/u01 /eirsapp/configuration/configuration.properties
typeset -A config # init array

while read line
do
    if echo $line | grep -F = &>/dev/null
    then
        varname=$(echo "$line" | cut -d '=' -f 1)
        config[$varname]=$(echo "$line" | cut -d '=' -f 2-)
    fi
done < $conffile
conn1="mysql -h${config[ip]} -P${config[dbPort]} -u${config[dbUsername]} -p${config[dbPassword]}"
conn="mysql -h${config[ip]} -P${config[dbPort]} -u${config[dbUsername]} -p${config[dbPassword]} ${config[appdbName]}"

echo "creating app database."
${conn1} -e "CREATE DATABASE IF NOT EXISTS app;"
echo "app database successfully created!"

`${conn} <<EOFMYSQL
CREATE TABLE if not exists sys_param (
  id int NOT NULL AUTO_INCREMENT,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  description varchar(255) DEFAULT '',
  tag varchar(255) DEFAULT NULL,
  type int DEFAULT '0',
  value text,
  active int DEFAULT '0',
  feature_name varchar(255) DEFAULT '',
  remark varchar(255) DEFAULT '',
  user_type varchar(255) DEFAULT '',
  modified_by varchar(255) DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE KEY tag (tag)
);

CREATE TABLE ip_location_country_ipv4 (
  id int NOT NULL AUTO_INCREMENT,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  start_ip_number int unsigned DEFAULT NULL,
  end_ip_number int unsigned NOT NULL,
  country_code char(2) DEFAULT NULL,
  country_name varchar(64) DEFAULT NULL,
  data_source varchar(10) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE ip_location_country_ipv6 (
  id int NOT NULL AUTO_INCREMENT,
  created_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  start_ip_number decimal(39,0) unsigned DEFAULT NULL,
  end_ip_number decimal(39,0) unsigned NOT NULL,
  country_code char(2) DEFAULT NULL,
  country_name varchar(64) DEFAULT NULL,
  data_source varchar(10) DEFAULT NULL,
  PRIMARY KEY (id)
);

insert into sys_param (description, tag, value, feature_name) SELECT 'The key used for api call to ip location database to query country for an IP.', 'ipCountryApiKey', '63E2F05CC085E319664B99D6AB5D4426', 'Ip location' FROM dual WHERE NOT EXISTS ( SELECT * FROM sys_param WHERE tag = 'ipCountryApiKey');
insert into sys_param (description, tag, value, feature_name) SELECT 'The url used for api call to ip location database to query country for an IP.', 'ipCountryApiUrl', 'https://api.ip2location.io/?key=<key>&ip=<ip>&format=json', 'Ip location' FROM dual WHERE NOT EXISTS ( SELECT * FROM sys_param WHERE tag = 'ipCountryApiUrl');

EOFMYSQL`

echo "Tables creation completed."
echo "                                             *
						  ***
						 *****
						  ***
						   *                           "
echo "********************Thank You DB Process is completed now for IP Location API Module*****************"