import { BrawlerList } from "@/app/brawlers/brawler-list";
import { BrawlerAdd } from "./brawler-add";
import { GearList } from "./gear-list";

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