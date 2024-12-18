import NewBattleEventRequest from "@/model/request/NewBattleEventRequest";

export default async function registerEvent(request: NewBattleEventRequest) {
    const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/events`);
    console.log(`POST to ${url}`);
    
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        throw new Error(`Failed to register event. response: ${response}`);
    }
} 