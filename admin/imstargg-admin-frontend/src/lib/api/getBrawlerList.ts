import Brawler from "@/model/Brawler";

interface BrawlerListResponse {
  content: Brawler[];
}

export async function getBrawlerList(): Promise<Brawler[]> {

    const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/brawlers`);
    console.log(`Fetch from ${url}`);
    
    const response = await fetch(url, {
      next: {
          tags: ['brawlers']
      }
    });
    
    if (!response.ok) {
      throw new Error(`Failed to fetch brawler data. response: ${response}`);
    }
    
    const data = await response.json() as BrawlerListResponse;

    return data.content;
  }
  