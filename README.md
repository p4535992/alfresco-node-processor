# Alfresco Node Processor
_Do things with nodes._

A modern, threaded and easily customizable Spring Boot Application that - given a means for collecting nodes - do something with them.

Think about this as a template for your application.

Pull requests are welcome!
## Customize
If none of the predefined Collectors/Processors meet your needs, simply write your own by extending the abstract ones. Just inject the required handler (e.g., NodesApi) and override the relevant methods.
### Collecting nodes
#### QueryNodeCollector
The QueryNodeCollector takes an Alfresco FTS query, execute it on a separate thread and feed the queue:
```json
"collector": {
  "name": "QueryNodeCollector",
  "args": {
    "query": "PATH:'/app:company_home/*' AND TYPE:'cm:folder'"
  }
}
```
#### NodeListCollector
The NodeListCollector takes an input file containing a list of node-id with each id on a separate line, e.g.:
```
e72b6596-ec2e-4279-b490-3a03b119d8de
d78c0036-15c0-43cf-89e4-cd198d14b626
1a7ecc34-de06-45ed-85c0-76f8355f3724
```
and the path of the file need to be specified in the config:
```json
"collector": {
    "name": "NodeListCollector",
    "args": {
      "nodeListFile": "/tmp/node-ids.txt"
    }
  }
```
### Processing nodes
#### DeleteNodeProcessor
Delete the collected nodes, set the `permanent` flag to true if you want to delete the nodes directly rather than move them into the trashcan:
```json
"processor": {
  "name": "DeleteNodeProcessor",
  "args": {
    "permanent": true
  }
}
```     
#### AddAspectsAndSetPropertiesProcessor
Add a list of aspects and apply a map of properties to the collected nodes:
```json
"processor": {
  "name": "AddAspectsAndSetPropertiesProcessor",
  "args": {
    "properties": {
      "cm:publisher": "saidone",
      "cm:contributor": "saidone"
    },
    "aspects": [
      "cm:dublincore"
    ]
  }
}
```
#### SetPermissionsProcessor
Apply a list of permissions and set inheritance flag to the collected nodes:
```json
"processor": {
  "name": "SetPermissionsProcessor",
  "args": {
    "permissions": {
      "isInheritanceEnabled": false,
      "locallySet": [
        {
          "authorityId": "GROUP_EVERYONE",
          "name": "Collaborator",
          "accessStatus": "ALLOWED"
        }
      ]
    }
  }
}
```
#### Custom processor
Custom processors can be easily created by extending the AbstractNodeProcessor and overwriting the `processNode` method:
```java
@Component
@Slf4j
public class LogNodeNameProcessor extends AbstractNodeProcessor {

    @Autowired
    private NodesApi nodesApi;

    @Override
    public void processNode(String nodeId, Config config) {
        var node = Objects.requireNonNull(nodesApi.getNode(nodeId, null, null, null).getBody()).getEntry();
        log.debug("node name --> {}", node.getName());
    }

}
```
## Build
Java and Maven required

`mvn package -DskipTests -Dlicense.skip=true`

look at the `build.sh` or `build.bat` scripts for creating a convenient distribution package.
## Application global config
Application is configured through these ENV variables, hence `run.sh` and `run.bat` scripts are a convenient way to run the program (default value in parentheses):
- SPRING_PROFILES_ACTIVE (`dev`)
- ALFRESCO_BASE_PATH (`http://localhost:8080`)
- ALFRESCO_USERNAME (`admin`)
- ALFRESCO_PASSWORD (`admin`)
- QUEUE_SIZE (`1000`)
- CONSUMER_THREADS (`4`)
- CONSUMER_TIMEOUT (`5000`)
## Testing
For integration tests just change configuration and point it to an existing Alfresco installation, or use `alfresco.(sh|bat)` script to start it with docker.
## Run
`$ java -jar alfresco-node-processor.jar ./example.json` or use the provided `run.sh` and `run.bat` scripts.
## License
Copyright (c) 2023 Saidone

Distributed under the GNU General Public License v3.0
