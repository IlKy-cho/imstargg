import { getBrawlers } from "@/lib/api/brawler";
import {StarPowerList} from "./_components/starpower-list";

export default async function StarPowersPage() {
  const brawlers = await getBrawlers();
  return (
    <div className="flex flex-col">
      <h1>스타파워 목록</h1>
      <StarPowerList brawlers={brawlers} />
    </div>
  );
}