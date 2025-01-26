import Link from "next/link";
import Image from 'next/image';
import MainNav from "@/components/main-nav";
import {meta} from "@/config/site";

export default function SiteHeader() {

  return (
    <header className="bg-zinc-900 text-white h-14 px-4 w-full z-50">
      <div className="max-w-7xl mx-auto h-full flex items-center">
        <div className="flex items-center gap-6">
          <Link href="/" className="flex items-center gap-2 hover:opacity-80 transition-opacity">
            <Image src={meta.logo} alt="ImStarGG Logo" width={50} height={50}/>
            <h1 className="text-xl font-bold">ImStarGG</h1>
          </Link>
          <MainNav />
        </div>
      </div>
    </header>
  );
}