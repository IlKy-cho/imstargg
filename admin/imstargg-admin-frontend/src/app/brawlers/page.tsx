import {BrawlerList} from "./_components/brawler-list";
import {BrawlerAdd} from "./_components/brawler-add";
import {getBrawlers} from "@/lib/api/brawler";

export default async function BrawlersPage() {
  const brawlers = await getBrawlers();
  return (
    <>
      <div className="flex justify-between items-center">
        <h1>브롤러 목록</h1>
        <div className="space-x-2">
          <BrawlerAdd />
        </div>
      </div>
      <BrawlerList brawlers={brawlers}/>
    </>
  );
}