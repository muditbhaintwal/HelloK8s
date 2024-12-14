# Hello k8s

It's a hello to k8s. It's an effort to play with k8s concepts of deployments ports services etc.

## Steps

* build project

* create image

* push it to docker hub

* deploy it to k8s

**api to hit  http://localhost:80/v1/time**

**Most frequently used commands**

```bash

gradlew build jib

kubectl get deployments
kubectl delete deployments hello-k8s-deployment
kubectl apply -f src/k8s/deployment.yaml
kubectl get pod -o wide

kubectl get svc
kubectl apply -f src/k8s/service.yaml

kubectl logs 
```



## Notes


This exposes the host machine's port 9000 to container's  port 8080.


`docker run -p 9000:8080 my-image`

http://localhost:9000/v1/time


**To get container ip**

`docker ps

docker inspect -f '{{.NetworkSettings.IPAddress}}' 1fea6306b1ca`

I am not able to access app via container IP.



Pushing an image to repo
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

**The Deployment file (deployment.yaml) and the Service file (service.yaml) are matched through the selector field in the Service file and the labels field in the Deployment file.**

In the Deployment file, the template has a label app: hello-k8s:



----------------------------
**Imp kubectl commands**

https://github.com/dennyzhang/cheatsheet-kubernetes-A4







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

**Debugging pods**


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
containerPort in the deployment.yaml = container is listening to this port





in service yaml
```bash
    port: 80 # Service exposes to the "external" world
    targetPort: 8080
```
**port and targetPort in the service.yaml file = This means that the service is forwarding traffic from port 80 to port 8080 on the container.**





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

## JIB VS DOCKERFILE

Jib is a Java-based tool that uses the Java runtime to create Docker images, without requiring a Docker installation.


**Do I need docker for jib image creation?**

No, you don't need Docker installed on your machine to use Jib for image creation.

Jib is a Java-based tool that uses the Java runtime to create Docker images, without requiring a Docker installation. Jib uses the Java Virtual Machine (JVM) to execute the build process and create the image, so you only need to have Java installed on your machine.

Here are the benefits of not requiring Docker:

* Simplified setup: You don't need to install Docker on your machine, which can be a complex process, especially on Windows or macOS.

* Faster builds: Jib can build images faster than Docker, since it doesn't require the overhead of running a Docker daemon.

* Improved security: By not requiring Docker, Jib reduces the attack surface of your machine, since you don't need to expose the Docker daemon to the network.


**Dockerfile**

**Pros**:
* Well-established and widely used
* Supports a wide range of features, including multi-stage builds and volume mounts
* Can be used with a variety of Docker tools and plugins

**Cons**:
* Can be verbose and difficult to maintain, especially for complex builds
* Requires manual management of dependencies and build steps
* Can lead to large and bloated images if not optimized properly


**Jib**

**Pros**:
* Simplifies the build process and reduces verbosity
* Automatically manages dependencies and build steps
* Produces smaller and more efficient images
* Integrates well with popular build tools like Maven and Gradle
**Cons**:
* Limited support for advanced Docker features, such as multi-stage builds and volume mounts
* May require additional configuration for complex builds
* Still a relatively new tool, with a smaller community and fewer resources

**When to use Dockerfile**

* You need to use advanced Docker features, such as multi-stage builds or volume mounts.
* You have a complex build process that requires manual management of dependencies and build steps.
* You prefer a more traditional and well-established build process.

**When to use Jib**

* You want a simpler and more streamlined build process.
* You're using a popular build tool like Maven or Gradle and want to integrate Jib into your existing workflow.
* You're looking for a tool that can produce smaller and more efficient images.

Ultimately, the choice between Dockerfile and Jib depends on your specific needs and preferences. If you're already comfortable with Dockerfile and have a complex build process, you may want to stick with it. However, if you're looking for a simpler and more streamlined build process, Jib may be a good choice.

Here's a rough estimate of when to use each tool:

Dockerfile: 70-80% of use cases (complex builds, advanced Docker features)
Jib: 20-30% of use cases (simple builds, streamlined workflow)
Keep in mind that this is just a rough estimate, and the best tool for your project will depend on your specific needs and requirements.



https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#quickstart


## Setting gitlab in local

use docker way..
Step 1: Pull the GitLab Docker image, This will download the latest available GitLab Community Edition (CE) image.
`docker pull gitlab/gitlab-ce:latest`


Step 2: Run the GitLab container
Run the following command to start a new GitLab container:

`docker run -d --name gitlab -p 81:81 -p 443:443 -p 22:22 -v /var/gitlab/data:/var/gitlab/data -v /var/gitlab/logs:/var/log/gitlab -v /var/gitlab/onfig:/etc/gitlab gitlab/gitlab-ce:latest`

Here's what each part of the command does:

* -d runs the container in detached mode (i.e., in the background)
* --name gitlab gives the container the name "gitlab"
* -p 80:80 maps port 80 on the host machine to port 80 in the container (for HTTP access)
* -p 443:443 maps port 443 on the host machine to port 443 in the container (for HTTPS access)
* -p 22:22 maps port 22 on the host machine to port 22 in the container (for SSH access)
* -v mounts volumes to persist data between container restarts:

      
      /var/gitlab/data:/var/gitlab/data for GitLab data
* 
      /var/gitlab/logs:/var/log/gitlab for GitLab logs
* 
      /var/gitlab/config:/etc/gitlab for GitLab configuration

Step 3: Access your GitLab instance

Once the container is running, you can access your GitLab instance by visiting http://localhost in your web browser. You'll see the GitLab login page.

Initial setup

After logging in for the first time, you'll need to set up your GitLab instance by creating an admin user and setting a password.
## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)