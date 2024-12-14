# Hello k8s

Its a hello to k8s. just to get into concepts of deployments ports services etc.

## Steps

build project

create image

push it to docker hub

deploy it to k8s

## notes


This exposes the host machine's port 9000 to container's  port 8080.


docker run -p 9000:8080 my-image

http://localhost:9000/v1/time


To get conatiner ip

docker ps

docker inspect -f '{{.NetworkSettings.IPAddress}}' 1fea6306b1ca

I am not able to access app via conatiner ip.



pushing an image to repo
------------------------

To push an image, you first need to create a repository on Docker Hub.


1- login
```bash
docker login -u your-dockerhub-username
```
2- tag
```bash
docker tag local-image-name your-dockerhub-username/repository-name:tag

docker tag hello-k8s muditbhaintwal/mudit-repo:v1.0.0
```

3-push
```bash
docker push your-dockerhub-username/repository-name:tag

docker push muditbhaintwal/mudit-repo:v1.0.0
```

-------------------

In a typical Kubernetes setup, you would need to apply both deployment.yaml and service.yaml separately.

The deployment.yaml file defines the deployment, which manages the rollout of your application, including the container specification you provided earlier.


You can apply both files using the kubectl apply command. The sequence doesn't matter, but it's a good practice to apply the deployment first, followed by the service.

```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

This order makes sense because the service relies on the deployment being created first. However, Kubernetes is designed to handle dependencies and will wait for the deployment to be created before creating the service, even if you apply the service first.

----------------------

# The Deployment file (deployment.yaml) and the Service file (service.yaml) are matched through the selector field in the Service file and the labels field in the Deployment file.
#
#In the Deployment file, the template has a label app: hello-k8s:



----------------------------
kube ctl commands

https://github.com/dennyzhang/cheatsheet-kubernetes-A4

Examples:
```bash

# Get commands with basic output
kubectl get services                          # List all services in the namespace
kubectl get pods --all-namespaces             # List all pods in all namespaces
kubectl get pods -o wide                      # List all pods in the current namespace, with more details
kubectl get deployment my-dep                 # List a particular deployment
kubectl get pods                              # List all pods in the namespace
kubectl get pod my-pod -o yaml                # Get a pod's YAML
kubectl get namespaces

Scaling resources

kubectl scale --replicas=3 rs/foo                                 # Scale a replicaset named 'foo' to 3
kubectl scale --replicas=3 -f foo.yaml                            # Scale a resource specified in "foo.yaml" to 3


Deleting resources
kubectl delete -f ./pod.json                                      # Delete a pod using the type and name specified in pod.json
kubectl delete pod unwanted --now                                 # Delete a pod with no grace period
kubectl delete pod,service baz foo                                # Delete pods and services with same names "baz" and "foo"



Interacting with running Pods
kubectl logs my-pod                                 # dump pod logs (stdout)
kubectl logs -l name=myLabel                        # dump pod logs, with label name=myLabel (stdout)
kubectl logs my-pod --previous                      # dump pod logs (stdout) for a previous instantiation of a container


List everything	kubectl get all --all-namespaces
Get all services	kubectl get service --all-namespaces
Get all deployments	kubectl get deployments --all-namespaces
Show nodes with labels	kubectl get nodes --show-labels


# Describe commands with verbose output
kubectl describe nodes my-node
kubectl describe pods my-pod

# List Services Sorted by Name
kubectl get services --sort-by=.metadata.name	


  # List all pods in ps output format
  kubectl get pods

  # List all pods in ps output format with more information (such as node name)
  kubectl get pods -o wide

  # List a single replication controller with specified NAME in ps output format
  kubectl get replicationcontroller web

  # List deployments in JSON output format, in the "v1" version of the "apps" API group
  kubectl get deployments.v1.apps -o json

  # List a single pod in JSON output format
  kubectl get -o json pod web-pod-13je7

  # List a pod identified by type and name specified in "pod.yaml" in JSON output format
  kubectl get -f pod.yaml -o json

  # List resources from a directory with kustomization.yaml - e.g. dir/kustomization.yaml
  kubectl get -k dir/

  # Return only the phase value of the specified pod
  kubectl get -o template pod/web-pod-13je7 --template={{.status.phase}}

  # List resource information in custom columns
  kubectl get pod test-pod -o custom-columns=CONTAINER:.spec.containers[0].name,IMAGE:.spec.containers[0].image

  # List all replication controllers and services together in ps output format
  kubectl get rc,services

  # List one or more resources by their type and names
  kubectl get rc/web service/frontend pods/web-pod-13je7

  # List the 'status' subresource for a single pod
  kubectl get pod web-pod-13je7 --subresource status

Options:
    -A, --all-namespaces=false:
        If present, list the requested object(s) across all namespaces. Namespace in current context is ignored even
        if specified with --namespace.


kubectl apply -f deployment.yaml
kubectl get deployments
kubectl delete deployments hello-k8s-deployment


# List Events sorted by timestamp
kubectl get events --sort-by=.metadata.creationTimestamp

# List all warning events
kubectl events --types=Warning

```



-------------------------------------

debugging pods


To access the app from a browser, you would need to:

1- Get the external IP address of the LoadBalancer service:
```bash
kubectl get svc -o wide
```
This will display the external IP address of the service.

2- Open a web browser and navigate to http://<external-ip-address>:80, replacing <external-ip-address> with the actual IP address you obtained in step 1.
Note that the containerPort in the deployment.yaml file should match with the targetPort in the service.yaml file. 

in deployment.yaml
```bash
        ports:
        - containerPort: 8080
```
containerPort in the deployment.yaml = conatiner is listening to this port





in service yaml
```bash
    port: 80 # Service exposes to the "external" world
    targetPort: 8080
```
port and targetPort in the service.yaml file = This means that the service is forwarding traffic from port 80 to port 8080 on the container. 





If you've fixed the port mismatch and the service is still not accessible, here are a few more things to check:

Check the service status: 
Run kubectl get svc to ensure the service is running and has an external IP address.

Check the pod status: 
Run kubectl get pods to ensure the pod is running and has no errors.

Check the container logs: Run kubectl logs <pod-name> to see if there are any errors or issues with the container.
Check the firewall rules: If you're running on a cloud provider, check the firewall rules to ensure that incoming traffic on port 80 is allowed.


Try accessing the service using the NodePort: If the LoadBalancer is not working, try accessing the service using the NodePort. You can find the NodePort by running kubectl get svc -o wide. Then, access the service using http://<node-ip-address>:<node-port>.
If none of these troubleshooting steps help, please provide more details about your environment and setup, and I'll do my best to assist you further.


--------------------------




## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
