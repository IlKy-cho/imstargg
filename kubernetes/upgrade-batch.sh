sudo microk8s helm upgrade -n app battle-event-update-job-batch ./helm/batch -f battle-event-update-job-values.yaml
sudo microk8s helm upgrade -n app brawler-count-job-batch ./helm/batch -f brawler-count-job-values.yaml
sudo microk8s helm upgrade -n app brawler-ranking-job-batch ./helm/batch -f brawler-ranking-job-values.yaml
sudo microk8s helm upgrade -n app daily-job-batch ./helm/batch -f daily-job-values.yaml
sudo microk8s helm upgrade -n app player-ranking-job-batch ./helm/batch -f player-ranking-job-values.yaml
sudo microk8s helm upgrade -n app player-update-job-batch ./helm/batch -f player-update-job-values.yaml
