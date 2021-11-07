package org.musicbox.modules.paginator.templates;


import net.dv8tion.jda.api.entities.MessageEmbed;

public interface PageTemplate<I> {

   I getTemplateSource();
   
   MessageEmbed[] getAllPages();
   
}
