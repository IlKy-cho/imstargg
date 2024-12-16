import Image from "next/image";

export default function Home() {
  return (
    <main className="flex flex-col items-center justify-center p-24">
      <div className="mb-8">
        <Image
          src="/logo.png"
          alt="ImStarGG 로고"
          width={200}
          height={100}
          priority
        />
      </div>
    </main>
  );
}
