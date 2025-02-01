import Image from "next/image";
import {meta} from "@/config/site";
import Link from "next/link";
import {policyItems} from "@/config/docs";

export default function SiteFooter() {
  return (
    <footer className="bg-zinc-100 py-6 sm:py-10 mt-6 sm:mt-10">
      <div className="max-w-screen-lg mx-auto px-4 space-y-4">
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
          <Menu title="Resources" elements={[
            {title: policyItems.agreement.label, href: policyItems.agreement.href},
          ]}
          />
        </div>
        <div className="text-zinc-600 text-xs">
          <p>이메일: {meta.email}</p>
        </div>
        <p className="text-zinc-600 text-xs">
          This content is not affiliated with, endorsed, sponsored, or specifically approved by Supercell and Supercell is not responsible for it. For more information see <Link href="https://www.supercell.com/fan-content-policy" target="_blank">Supercell&apos;s Fan Content Policy</Link>.
        </p>
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
        <Link key={element.title} href={element.href}>
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