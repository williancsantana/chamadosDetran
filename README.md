Configuração do Data Source no WildFly

Adicionar esses DataSources na Seção

<subsystem xmlns="urn:jboss:domain:datasources:3.0">
            <datasources>
            
do arquivo standalone.xml, que se encontra no caminho: WILDFLY_HOME/standalone/configuration

<datasource jndi-name="java:jboss/chamadosDS" pool-name="chamadosDS" enabled="true">
                    <connection-url>jdbc:postgresql://localhost:5432/detran</connection-url>
                    <driver>postgresql</driver>
                    <pool>
                        <min-pool-size>1</min-pool-size>
                        <max-pool-size>20</max-pool-size>
                    </pool>
                    <security>
                        <user-name>postgres</user-name>
                        <password>qwe123</password>
                    </security>
                    <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker"/>
                        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter"/>
                    </validation>
                </datasource>
                <datasource jndi-name="java:jboss/detranErpDS" pool-name="detranErpDS" enabled="true">
                    <connection-url>jdbc:sqlserver://10.86.99.118:1434;DatabaseName=detran-erp</connection-url>
                    <driver>sqlserver</driver>
                    <pool>
                        <min-pool-size>1</min-pool-size>
                        <max-pool-size>20</max-pool-size>
                    </pool>
                    <security>
                        <user-name>detran-erp</user-name>
                        <password>detran##linkcon##</password>
                    </security>
                    <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mssql.MSSQLValidConnectionChecker"/>
                    </validation>
                </datasource>
                <drivers>
                    <driver name="h2" module="com.h2database.h2">
                        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                    </driver>
                    <driver name="postgresql" module="org.postgresql">
                        <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
                    </driver>
                    <driver name="sqlserver" module="com.microsoft">
                        <xa-datasource-class>com.microsoft.sqlserver.jdbc.SQLServerDriver</xa-datasource-class>
                    </driver>
                </drivers>
                
                
Depois realizar o download do arquivo drivers.zip e extrair na pasta

WILDFLY_HOME/modules/system/layers/base