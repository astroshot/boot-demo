# boot-mybatis-generator

## Introduction

Mybatis Generator is a code generator for Java DAO, which generates simple CRUD methods. But its code does not support complex SQL query, for example pagination. So a simple extension supporting pagination is developed.

## Usage

This project is embedded as a module in `boot-demo` project. It is included by module `boot-dao` in `pom.xml`. 

1. Configure database connection parameters in file `boot-my-batis-generator/datasource.properties`;
2. Edit file `boot-dao/src/main/resources/generatorConfig.xml`, modify java model package and location;
3. Modify java mapper xml files location;
4. Modify generated java mapper model location;
5. Modify target tables in your database;
