import {getBrawlers} from "@/lib/api/brawler";
import BrawlerList from "@/components/brawler-list";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { WrenchIcon } from "lucide-react";

export default async function BrawlersPage() {
  const brawlerList = await getBrawlers();

  return (<div className="flex flex-col items-center max-w-7xl mx-auto p-4">
    <h1 className="text-3xl font-bold mb-6 text-zinc-800 border-b-2 border-zinc-200 pb-2">브롤러 목록</h1>
    <Alert className="max-w-2xl">
      <WrenchIcon className="w-4 h-4"/>
      <AlertTitle>
        브롤러 통계는 현재 개발 중입니다.
      </AlertTitle>
    </Alert>
    <BrawlerList brawlerList={brawlerList}/>
  </div>);
}