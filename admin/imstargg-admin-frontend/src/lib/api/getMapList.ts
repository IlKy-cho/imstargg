import Brawler from "@/model/Brawler";
import BattleMap from "@/model/BattleMap";

interface BattleMapListResponse {
  content: BattleMap[];
}

export default async function getMapList(): Promise<BattleMap[]> {

  const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/maps`);
  console.log(`Fetch from ${url}`);

  const response = await fetch(url, {
    next: {
      tags: ['battle-maps']
    }
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch brawler data. response: ${response}`);
  }

  const data = await response.json() as BattleMapListResponse;

  return data.content;
}
