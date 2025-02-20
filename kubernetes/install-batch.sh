sudo microk8s helm install -n app battle-event-update-job-batch ./helm/batch -f battle-event-update-job-values.yaml
sudo microk8s helm install -n app brawler-count-job-batch ./helm/batch -f brawler-count-job-values.yaml
sudo microk8s helm install -n app brawler-ranking-job-batch ./helm/batch -f brawler-ranking-job-values.yaml
sudo microk8s helm install -n app event-rotation-job-batch ./helm/batch -f event-rotation-job-values.yaml
sudo microk8s helm install -n app player-ranking-job-batch ./helm/batch -f player-ranking-job-values.yaml
sudo microk8s helm install -n app player-update-job-batch ./helm/batch -f player-update-job-values.yaml

sudo microk8s helm install -n app today-statistics-job-batch ./helm/batch -f today-statistics-job-values.yaml
sudo microk8s helm install -n app one-day-ago-statistics-job-batch ./helm/batch -f one-day-ago-statistics-job-values.yaml
sudo microk8s helm install -n app two-days-ago-statistics-job-batch ./helm/batch -f two-days-ago-statistics-job-values.yaml
sudo microk8s helm install -n app three-days-ago-statistics-job-batch ./helm/batch -f three-days-ago-statistics-job-values.yaml
