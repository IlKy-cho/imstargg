import {Separator} from "@/components/ui/separator";
import Image from "next/image";
import {meta} from "@/config/site";
import Link from "next/link";

export default function SiteFooter() {
  return (
    <footer className="bg-zinc-100 py-6 sm:py-10 mt-6 sm:mt-10">
      <div className="max-w-screen-lg mx-auto px-4 space-y-6">
        <div className="flex items-center">
          <Image
            src={meta.logo}
            alt="ImStarGG Logo"
            width={24}
          />
          <span className="text-zinc-700 font-bold">ImStarGG</span>
        </div>
        <div className="flex gap-4">
          <Menu title="ImStarGG" elements={[
            {title: "About", href: "/about"},
          ]}/>
        </div>
        <div className="text-zinc-600 text-xs">
          <p>ImStarGG 입니다. 열심히 만들어보겠습니다.</p>
          <p>이메일: imstargg2024@gmail.com</p>
        </div>
      </div>
    </footer>
  );
}

function Menu({title, elements}: Readonly<{ title: string, elements: MenuElement[] }>) {
  return (
    <div className="flex flex-col gap-1">
      <div className="text-sm font-bold text-zinc-700">
        {title}
      </div>
      {elements.map((element) => (
          <Link href={element.href}>
            <div className="text-sm text-zinc-500">
              {element.title}
            </div>
          </Link>
      ))}
    </div>
  );
}

interface MenuElement {
  title: string;
  href: string;
}