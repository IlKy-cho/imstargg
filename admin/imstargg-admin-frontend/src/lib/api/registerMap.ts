import NewMapRequest from "@/model/request/NewMapRequest";

export default async function registerMap(request: NewMapRequest) {
    const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/maps`);
    console.log(`POST to ${url}`);
    
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        throw new Error(`Failed to register map. response: ${response}`);
    }
}