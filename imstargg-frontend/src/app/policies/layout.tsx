import { Button } from "@/components/ui/button";
import { policyItems } from "@/config/docs";
import Link from "next/link";

export default function PoliciesLayout({children}: Readonly<{children: React.ReactNode}>) {
  return (
    <div className="my-4 space-y-4 p-1">
      <div className="flex gap-1">
        <Link href={policyItems.agreement.href}>
          <Button>
            {policyItems.agreement.label}
          </Button>
        </Link>
      </div>
      <div className="p-1">
        {children}
      </div>
    </div>
  );
}