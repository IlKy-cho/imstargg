import {BrawlerList} from "@/app/brawlers/brawler-list";

export default async function BrawlersPage() {

  return (
    <main>
      <h1>브롤러 목록</h1>
      <BrawlerList />
    </main>
  );
}