import Player from "@/model/Player";

interface PlayerResponse {
  tag: string;
  name: string;
  nameColor: string;
  iconId: number;
  trophies: number;
  highestTrophies: number;
  clubTag: string | null;
  updatedAt: string;
}

export async function getPlayer(tag: string): Promise<Player> {
  console.log(process.env.NEXT_PUBLIC_API_BASE_URL);
  const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/players/${tag}`);
  console.log(`Fetch from ${url}`);
  const response = await fetch(url, {
    next: {
        tags: ['players', tag]
    }
  });
  
  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Player not found');
    }
    throw new Error('Failed to fetch player data');
  }
  
  const data = await response.json() as PlayerResponse;
  return {
    ...data,
    updatedAt: new Date(data.updatedAt)
  };
}
