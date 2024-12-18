import { BrawlerList } from "@/app/brawlers/brawler-list";
import { BrawlerAdd } from "./brawler-add";

export default async function BrawlersPage() {
  return (
    <main>
      <div className="flex justify-between items-center">
        <h1>브롤러 목록</h1>
        <BrawlerAdd />
      </div>
      <BrawlerList />
    </main>
  );
}