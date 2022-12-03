# HOSPITAL

[Task](https://github.com/serothim/hospital/blob/master/TASK.md)


##### |JAVA VERSION 12|
##### MySQL database


#####  For a locally example with a using Docker:
	docker run --name hospital_mysql_8.0 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=hospital -d mysql:8.0


Use your favorite development tool for MySQL database, create a user, such as for example hospital_admin,
not to use the root account( the root account is not for this purpose ;-) )


	CREATE USER 'hospital_admin'@'%' IDENTIFIED BY 'admin';
	GRANT ALL PRIVILEGES ON hospital.* TO 'hospital_admin'@'%'
		WITH GRANT OPTION;
	
If you are using Docker, the IP address of your container can be found using:

	docker inspect hospital_mysql_8.0
	
In the end of output like that:


	                "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2", <----- This IP your container hospital_mysql_8.0
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:11:00:02",
                    "DriverOpts": null
                }
            }
        }
    }


#### Edit your run configuration:
##### Add environment variables:
	DATABASE_NAME = Your database name
	DB_PASSWORD = Your database password
	DB_SERVER_IP_ADDRESS =  IP address your database server
	DB_USERNAME = Your database username
    

### Run application.
    
 Adding an administrator account by default,
 which you can later delete or change.  
 Account by default created automatically if there 
 are no any other records in the users table.  

    email: admin@dmin.com
    password: admin
    
