sudo microk8s helm upgrade -n app battle-event-update-job-batch ./helm/batch -f battle-event-update-job-values.yaml
sudo microk8s helm upgrade -n app brawler-ranking-job-batch ./helm/batch -f brawler-ranking-job-values.yaml
sudo microk8s helm upgrade -n app event-rotation-job-batch ./helm/batch -f event-rotation-job-values.yaml
sudo microk8s helm upgrade -n app player-ranking-job-batch ./helm/batch -f player-ranking-job-values.yaml
sudo microk8s helm upgrade -n app player-renewal-failed-job-batch ./helm/batch -f player-renewal-failed-job-values.yaml
sudo microk8s helm upgrade -n app player-update-job-batch ./helm/batch -f player-update-job-values.yaml

sudo microk8s helm upgrade -n app player-brawler-statstics-job-batch ./helm/batch -f playerBrawlerStatstics-values.yaml
sudo microk8s helm upgrade -n app brawlerBattleRankStatisticsJob-batch ./helm/batch -f brawlerBattleRankStatisticsJob-values.yaml
sudo microk8s helm upgrade -n app brawlerBattleResultStatisticsJob-batch ./helm/batch -f brawlerBattleResultStatisticsJob-values.yaml
sudo microk8s helm upgrade -n app brawlerEnemyBattleResultStatisticsJob-batch ./helm/batch -f brawlerEnemyBattleResultStatisticsJob-values.yaml
sudo microk8s helm upgrade -n app brawlerPairBattleRankStatisticsJob-batch ./helm/batch -f brawlerPairBattleRankStatisticsJob-values.yaml
sudo microk8s helm upgrade -n app brawlerPairBattleResultStatisticsJob-batch ./helm/batch -f brawlerPairBattleResultStatisticsJob-values.yaml