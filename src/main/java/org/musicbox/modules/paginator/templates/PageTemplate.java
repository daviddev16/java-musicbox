package org.musicbox.modules.paginator.templates;


import net.dv8tion.jda.api.EmbedBuilder;

public interface PageTemplate<I> {

   I getTemplateSource();
   
   EmbedBuilder[] getAllPages();
   
}
