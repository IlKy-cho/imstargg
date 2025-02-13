import React from "react";

type PageHeaderProps = {
  children: React.ReactNode;
}

export async function PageHeader(
  {children}: Readonly<PageHeaderProps>
) {
  return (
    <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
      <div className="flex-1">
        {children}
      </div>
    </div>
  );
}