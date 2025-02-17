# P2PBank

- Showcase of a Peer-to-Peer project.

## Technologies

- Java
- Berkley Socket
- MySQL

## Requirements

- Java 17+
- Internet connection

## Config

```properties
log.file.path=log/log.txt
database.url=jdbc:mysql://sql.daniellinda.net:3306/ptpbnk
database.user=remote
database.password=hm3C4iLL+
server.port=65525
server.thread.pool.size=2
server.client.scan.threads=50
client.scan.port.range=65525,65535
#incoming socket connection/session inactive timeout (afk)
#60 for testing purposes
#seconds
server.client.timeout=60
#outcomming socket connection/session timeout before declaring host dead
#150+ for slow responding hosts/network
#milliseconds
server.client.scan.timeout=150
#after connection init alive host timeout
#milliseconds
server.client.connect.timeout=1000
#after connection and command execution alive host timeout
#milliseconds
server.client.read.timeout=2500
#complete handler task timeout
#value=(server.client.scan.timeout * 100) in case of scanning needs
#milliseconds
handler.task.timeout=15000
```

## Start-up & Install

- download from `https://daniellinda.net/bank.zip`
- navigate to installation folder
- start command line in said folder
- execute startup script written below
- default location of a config is next to the executable

- `java -jar bank.jar [config file path]`

## Reused code

- [Logger](https://github.com/WMeindW/WebServerDatabaseApi/tree/main/src/main/java/cz/meind/logger)
- [Init](https://github.com/WMeindW/WebServerDatabaseApi/tree/main/src/main/java/cz/meind/application)
- [Database](https://github.com/WMeindW/WebServerDatabaseApi/tree/main/src/main/java/cz/meind/database)
- [ObjectMapper](https://github.com/WMeindW/WebServerDatabaseApi/tree/main/src/main/java/cz/meind/service/mapper)
- [DatabaseAnnotationInterfaces](https://github.com/WMeindW/WebServerDatabaseApi/tree/main/src/main/java/cz/meind/interfaces)
- [Listener](https://github.com/WMeindW/AsynchronousWebServer/blob/master/src/main/java/cz/meind/service/asynch/Listener.java)
- [Daemon](https://github.com/WMeindW/AsynchronousWebServer/blob/master/src/main/java/cz/meind/service/asynch/Daemon.java)

## Sources

- Java doc
- [Berkley Sockets](https://csperkins.org/teaching/2007-2008/networked-systems/lecture04.pdf)
- [P2P](https://student.cs.uwaterloo.ca/~cs446/1171/Arch_Design_Activity/Peer2Peer.pdf)
- [WebServerDatabaseApi](https://github.com/WMeindW/WebServerDatabaseApi)
- [AsynchronousWebServer](https://github.com/WMeindW/AsynchronousWebServer)

## Commands

| Name                   | Code | Example                        | Expected            | Error          |
|------------------------|------|--------------------------------|---------------------|----------------|
| Bank code              | BC   | BC                             | BC `<ip>`           | ER `<message>` |
| Account create         | AC   | AC                             | AC `<account>/<ip>` | ER `<message>` |
| Account deposit        | AD   | AD `<account>/<ip>` `<number>` | AD                  | ER `<message>` |
| Account withdrawal     | AW   | AW `<account>/<ip>` `<number>` | AW                  | ER `<message>` |
| Account balance        | AB   | AB `<account>/<ip>`            | AB `<number>`       | ER `<message>` |
| Account remove         | AR   | AR `<account>/<ip>`            | AR                  | ER `<message>` |
| Bank (total) amount    | BA   | BA                             | BA `<number>`       | ER `<message>` |
| Bank number of clients | BN   | BN                             | BN `<number>`       | ER `<message>` |

## Testing

- tested by peers and collaborators on a private network
- issues with delay and/or firewall fixed
- timing and/or timeouts set per network performance

## Dependencies

- [mysql-connector](https://dev.mysql.com/doc/connector-j/en/)

## Issues

| Name                     | Description                                                                                   | Expected                     | Issue                   | Resolved/Fix                 |
|--------------------------|-----------------------------------------------------------------------------------------------|------------------------------|-------------------------|------------------------------|
| Ip resolving issues      | Could not resolve or get IP on system with no usable interfaces                               | Error message                | Crash                   | Yes                          |
| Scanning issue           | When scanning on low speed/bandwidth network, clients could not be resolved                   | Client resolving and scan    | Timeout occurred        | Increase timeout             |
| Single file logging      | File can get to big to work with (load into active memory while editing)                      | Separate files based on date | Large file              | No                           |
| Scanning algorithm speed | In low speed network timeouts have to be increased which causes single task time to skyrocket | Effective scanning           | 15+ second waiting time | Keep scan-timeout reasonable |

### Testers

- [Matěj Červenka](https://github.com/MatejCervenka)
- [Jakub Hofman](https://github.com/Mithynite)

### Reporting

- submit any issues to [GitHub project issue report page](https://github.com/WMeindW/PeerToPeerBank/issues/new)

### License

- educational use only