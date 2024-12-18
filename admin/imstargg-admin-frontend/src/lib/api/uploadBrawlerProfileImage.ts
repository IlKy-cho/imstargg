export default async function uploadBrawlerProfileImage(brawlStarsId: number, image: File) {
    const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/admin/api/brawlers/${brawlStarsId}/profile-image`);
    console.log(`PUT to ${url}`);
    
    const formData = new FormData();
    formData.append('image', image);

    const response = await fetch(url, {
        method: 'PUT',
        body: formData
    });

    if (!response.ok) {
        throw new Error(`Failed to upload brawler profile image. response: ${response}`);
    }
} 