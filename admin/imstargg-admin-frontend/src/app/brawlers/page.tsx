import { BrawlerList } from "./_components/brawler-list";
import { BrawlerAdd } from "./_components/brawler-add";
import { GearList } from "./_components/gear-list";

export default async function BrawlersPage() {
  return (
    <>
      <div className="flex justify-between items-center">
        <h1>브롤러 목록</h1>
        <div className="space-x-2">
          <GearList />
          <BrawlerAdd />
        </div>
      </div>
      <BrawlerList />
    </>
  );
}