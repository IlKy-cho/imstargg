sudo microk8s helm upgrade -n app battle-event-update-job-batch ./helm/batch -f battle-event-update-job-values.yaml
sudo microk8s helm upgrade -n app brawler-count-job-batch ./helm/batch -f brawler-count-job-values.yaml
sudo microk8s helm upgrade -n app brawler-ranking-job-batch ./helm/batch -f brawler-ranking-job-values.yaml
sudo microk8s helm upgrade -n app event-rotation-job-batch ./helm/batch -f event-rotation-job-values.yaml
sudo microk8s helm upgrade -n app player-ranking-job-batch ./helm/batch -f player-ranking-job-values.yaml
sudo microk8s helm upgrade -n app player-update-job-batch ./helm/batch -f player-update-job-values.yaml

sudo microk8s helm upgrade -n app today-statistics-job-batch ./helm/batch -f today-statistics-job-values.yaml
sudo microk8s helm upgrade -n app one-day-ago-statistics-job-batch ./helm/batch -f one-day-ago-statistics-job-values.yaml
sudo microk8s helm upgrade -n app two-days-ago-statistics-job-batch ./helm/batch -f two-days-ago-statistics-job-values.yaml
sudo microk8s helm upgrade -n app three-days-ago-statistics-job-batch ./helm/batch -f three-days-ago-statistics-job-values.yaml