"use client";

import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";

import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useState } from "react";
import { Form, FormControl, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { useRouter } from "next/navigation";
import { useRecentSearches } from "@/hooks/useRecentSearchs";
import Link from "next/link";
import { playerHref } from "@/config/site";
import { SearchIcon, SparkleIcon, XIcon } from "lucide-react";

const formSchema = z.object({
  nameOrTag: z.string().min(0).max(100, {
    message: "100자 이하로 입력해주세요."
  }),
});

export default function PlayerSearchForm() {
  const router = useRouter();
  const [isOpen, setIsOpen] = useState(false);
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      nameOrTag: "",
    },
  });

  function onSubmit(value: z.infer<typeof formSchema>) {
    if (value.nameOrTag.trim()) {
      const searchQuery = value.nameOrTag.startsWith('#') ?
        encodeURIComponent(value.nameOrTag) : `${value.nameOrTag}`;
      router.push(`/player/search?q=${searchQuery}`);
    }
  }

  return (
    <Collapsible open={isOpen} onOpenChange={setIsOpen}>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="flex gap-1">
          <CollapsibleTrigger asChild>
            <FormField
              control={form.control}
              name="nameOrTag"
              render={({ field }) => (
                <FormItem className="flex-1">
                  <FormControl>
                    <Input
                      className="bg-white"
                      placeholder="플레이어 태그 / 이름"
                      {...field}
                      onFocus={() => setIsOpen(true)}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </CollapsibleTrigger>
          <Button type="submit">검색</Button>
        </form>
      </Form>
      <CollapsibleContent>
        <RecentSearches />
      </CollapsibleContent>
    </Collapsible>
  );
}

function RecentSearches() {
  const { recentSearches, removeSearchTerm } = useRecentSearches();

  if (recentSearches.length === 0) {
    return null;
  }

  return (
    <div className="flex flex-col gap-1 border rounded-md p-1 bg-white text-sm">
      {recentSearches.map((search, index) => (
        <div key={index}>
          {search.type === 'player' ? (
            <RecentSearchPlayer 
              name={search.value.name} 
              tag={search.value.tag} 
              onRemove={() => removeSearchTerm({ type: 'player', value: { tag: search.value.tag, name: search.value.name } })}
            />
          ) : (
            <RecentSearchQuery 
              query={search.value} 
              onRemove={() => removeSearchTerm({ type: 'query', value: search.value })}
            />
          )}
        </div>
      ))}
    </div>
  );
}

function RecentSearchPlayer({ 
  name, 
  tag, 
  onRemove 
}: Readonly<{ 
  name: string, 
  tag: string, 
  onRemove: () => void 
}>) {
  return (
    <div className="flex items-center hover:bg-zinc-100">
      <Link href={playerHref(tag)} className="flex flex-1 items-center gap-1">
        <SparkleIcon className="h-4 w-4" />
        {name}<span className="text-zinc-500">#{tag}</span>
      </Link>
      <div 
        className="p-1 cursor-pointer"
        onClick={onRemove}
      >
        <XIcon className="h-4 w-4" />
      </div>
    </div>
  );
}

function RecentSearchQuery({ 
  query, 
  onRemove 
}: Readonly<{ 
  query: string, 
  onRemove: () => void 
}>) {
  return (
    <div className="flex items-center justify-between hover:bg-zinc-100">
      <Link href={`/player/search?q=${query}`} className="flex flex-1 items-center gap-1">
        <SearchIcon className="h-4 w-4" />
        {query}
      </Link>
      <div 
        className="p-1 cursor-pointer"
        onClick={onRemove}
      >
        <XIcon className="h-4 w-4" />
      </div>
    </div>
  );
}