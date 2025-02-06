import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getBattleLatestId, getPlayerLatestId } from "@/lib/api/statistics";
import Image from "next/image";

export default async function HomePage() {
  return (
    <div className="flex flex-col items-center justify-center mt-16 gap-4">
      <Image
        src="/logo.png"
        alt="ImStarGG 로고"
        width={200}
        height={100}
        priority
      />
      <div className="flex gap-2">
        <BattleLatestId />
        <PlayerLatestId />
      </div>
    </div>
  );
}

async function BattleLatestId() {
  const latestId = await getBattleLatestId();
  return (
    <Card>
      <CardHeader>
        <CardTitle>최근 배틀 ID</CardTitle>
      </CardHeader>
      <CardContent>
        {latestId}
      </CardContent>
    </Card>
  );
}

async function PlayerLatestId() {
  const latestId = await getPlayerLatestId();
  return (
    <Card>
      <CardHeader>
        <CardTitle>최근 플레이어 ID</CardTitle>
      </CardHeader>
      <CardContent>
        {latestId}
      </CardContent>
    </Card>
  );
}
