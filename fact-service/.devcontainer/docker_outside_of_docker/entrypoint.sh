#!/bin/sh

# For AMD64 / x86_64
[ $(uname -m) = x86_64 ] && curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.25.0/kind-linux-amd64
# For ARM64
[ $(uname -m) = aarch64 ] && curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.25.0/kind-linux-arm64
chmod +x ./kind
sudo mv ./kind /usr/local/bin/kind

kind delete cluster

if [ "$(docker inspect -f '{{.State.Running}}' "kind-registry" 2> /dev/null || true)" != 'true' ]; then
	docker run -d --restart=always -p 5000:5000 --name "kind-registry" registry:2
fi

sed "s/\t/  /g" <<EOF | kind create cluster --config=-
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
	extraPortMappings:
	- containerPort: 31112
		hostPort: 8080
EOF

if [ "$(docker inspect -f='{{json .NetworkSettings.Networks.kind}}' "kind-registry")" = 'null' ]; then
	docker network connect "kind" "kind-registry"
fi

sed "s/\t/  /g" <<EOF | kubectl apply -f -
apiVersion: v1
kind: ConfigMap
metadata:
	name: local-registry-hosting
	namespace: kube-public
data:
	localRegistryHosting.v1: |
		host: "localhost:5000"
		help: "https://kind.sigs.k8s.io/docs/user/local-registry/"
EOF
