sudo microk8s helm install -n app battle-event-update-job-batch ./helm/batch -f battle-event-update-job-values.yaml
sudo microk8s helm install -n app brawler-ranking-job-batch ./helm/batch -f brawler-ranking-job-values.yaml
sudo microk8s helm install -n app event-rotation-job-batch ./helm/batch -f event-rotation-job-values.yaml
sudo microk8s helm install -n app player-ranking-job-batch ./helm/batch -f player-ranking-job-values.yaml
sudo microk8s helm install -n app player-renewal-failed-job-batch ./helm/batch -f player-renewal-failed-job-values.yaml
sudo microk8s helm install -n app player-update-job-batch ./helm/batch -f player-update-job-values.yaml

sudo microk8s helm install -n app player-brawler-statstics-job-batch ./helm/batch -f playerBrawlerStatisticsJob-values.yaml
sudo microk8s helm install -n app brawler-battle-rank-statistics-job-batch ./helm/batch -f brawlerBattleRankStatisticsJob-values.yaml
sudo microk8s helm install -n app brawler-battle-result-statistics-job-batch ./helm/batch -f brawlerBattleResultStatisticsJob-values.yaml
sudo microk8s helm install -n app brawler-enemy-battle-result-statistics-job-batch ./helm/batch -f brawlerEnemyBattleResultStatisticsJob-values.yaml
sudo microk8s helm install -n app brawler-pair-battle-rank-statistics-job-batch ./helm/batch -f brawlerPairBattleRankStatisticsJob-values.yaml
sudo microk8s helm install -n app brawler-pair-battle-result-statistics-job-batch ./helm/batch -f brawlerPairBattleResultStatisticsJob-values.yaml
