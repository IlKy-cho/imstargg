import NewBrawlerRequest from "@/model/request/NewBrawlerRequest";

export default async function registerBrawler(request: NewBrawlerRequest) {
    const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/brawlers`);
    console.log(`POST to ${url}`);
    
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        throw new Error(`Failed to register brawler. response: ${response}`);
    }
} 