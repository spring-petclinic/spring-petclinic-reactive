## Build instructions

* The kubernetes manifests (with the exception of the `kubernetes/k8ssandra` directory) are created through kompose by converting the docker-compose file.
```bash
cd kubernetes
kompose convert -f ../docker-compose.yml
```
