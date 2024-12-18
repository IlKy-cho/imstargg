import NotRegisteredBattleEvent from "@/model/NotRegisteredBattleEvent";

interface NotRegisteredBattleEventListResponse {
    content: NotRegisteredBattleEvent[];
}

export default async function getNotRegisteredEventList(): Promise<NotRegisteredBattleEvent[]> {
    const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/not-registered-events`);
    console.log(`POST to ${url}`);
    
    const response = await fetch(url, {
        next: {
            tags: ['not-registered-events']
        }
    });

    if (!response.ok) {
        throw new Error(`Failed to register map. response: ${response}`);
    }

    const data = await response.json() as NotRegisteredBattleEventListResponse;

    return data.content;
}