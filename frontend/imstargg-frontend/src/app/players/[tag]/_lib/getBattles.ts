import Battle from "@/model/Battle";

interface BattleListResponse {
  content: BattleResponse[];
}

interface BattleResponse {
  battleTime: string;
  event: string;
  type: string;
  result: string;
  duration: number | null;
  rank: number | null;
  trophyChange: number | null;
  starPlayerTag: string | null;
  teams: BattlePlayerResponse[][];
}

interface BattlePlayerResponse {
  tag: string;
  name: string;
  brawler: string;
  brawlerPower: number;
  brawlerTrophies: number | null;
  brawlerTrophyChange: number | null;
}

export async function getBattles(tag: string, page: number = 1): Promise<Battle[]> {
  const encodedTag = encodeURIComponent(tag);
  const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/players/${encodedTag}/battles`);
  url.searchParams.append('page', page.toString());
  console.log(`Fetch from ${url}`);
  const response = await fetch(url, {
    next: {
      tags: ['players', tag, 'battles']
    }
  });
  
  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Player not found');
    }
    throw new Error('Failed to fetch battle data');
  }
  
  const data = await response.json() as BattleListResponse;
  return data.content.map(battle => ({
    ...battle,
    battleTime: new Date(battle.battleTime)
  }));
}
