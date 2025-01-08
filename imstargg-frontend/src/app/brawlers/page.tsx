import {getBrawlers} from "@/lib/api/brawler";
import BrawlerList from "@/components/brawler-list";
import {Alert, AlertTitle} from "@/components/ui/alert";
import {WrenchIcon} from "lucide-react";
import {Metadata} from "next";
import {metadataTitle} from "@/config/site";

export const metadata: Metadata = {
  title: metadataTitle("브롤스타즈 브롤러"),
  description: "브롤스타즈의 모든 브롤러 정보를 확인해보세요.",
};

export default async function BrawlersPage() {
  const brawlerList = await getBrawlers();

  return (
    <div className="flex flex-col items-center max-w-7xl mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6 text-zinc-800 border-b-2 border-zinc-200 pb-2">
        브롤러
      </h1>
      <Alert className="max-w-2xl">
        <WrenchIcon className="w-4 h-4"/>
        <AlertTitle>
          브롤러 통계는 현재 개발 중입니다.
        </AlertTitle>
      </Alert>
      <BrawlerList brawlerList={brawlerList}/>
    </div>
  );
}