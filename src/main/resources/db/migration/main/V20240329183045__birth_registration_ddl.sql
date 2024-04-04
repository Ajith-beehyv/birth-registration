CREATE TABLE eg_bt_registration(
  id varchar(64) PRIMARY KEY,
  tenantid varchar(64),
  applicationnumber varchar(64) UNIQUE,
  babyfirstname varchar(64),
  babylastname varchar(64),
  fatherid varchar(64),
  motherid varchar(64),
  doctorname varchar(64),
  hospitalmame varchar(64),
  placeofbirth varchar(64),
  timeofbirth bigint,
  createdby varchar(64),
  lastmodifiedby varchar(64),
  createdtime bigint,
  lastmodifiedtime bigint
);
 
 
CREATE TABLE eg_btr_app_address(
   id varchar(64) PRIMARY KEY,
   tenantId varchar(64),
   applicationnumber varchar(64),
   CONSTRAINT fk_eg_btr_app_address FOREIGN KEY (applicationnumber) REFERENCES eg_bt_registration(applicationnumber)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);
 
 
CREATE TABLE eg_bt_address(
   tenantid varchar(64),
   latitude FLOAT,
   longitude FLOAT,
   buildingname varchar(64),
   addressid varchar(64),
   addressnumber varchar(64),
   addressline1 varchar(256),
   addressline2 varchar(256),
   city varchar(64),
   pincode varchar(64),
   detail varchar(256),
   createdby varchar(64),
   lastmodifiedby varchar(64),
   createdtime bigint,
   lastmodifiedtime bigint,
   CONSTRAINT fk_eg_bt_address FOREIGN KEY (addressid) REFERENCES eg_btr_app_address(id)
     ON UPDATE CASCADE
     ON DELETE CASCADE
);