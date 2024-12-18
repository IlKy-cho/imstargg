import Gear from "@/model/Gear";

interface GearListResponse {
  content: Gear[];
}

export default async function getGearList(): Promise<Gear[]> {
  const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/gears`);
  console.log(`Fetch from ${url}`);
  
  const response = await fetch(url, {
    next: {
      tags: ['gears']
    }
  });
  
  if (!response.ok) {
    throw new Error(`Failed to fetch gear data. response: ${response}`);
  }
  
  const data = await response.json() as GearListResponse;
  return data.content;
} 