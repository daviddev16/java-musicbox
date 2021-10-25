package org.musicbox.core.module;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Modules {

   private static Logger logger = LoggerFactory.getLogger(Modules.class);
   private static Modules modulesInstance;

   private List<CoreModule> modules;

   private Modules() throws InstanceAlreadyExistsException {

      if(modulesInstance != null)
         throw new InstanceAlreadyExistsException("Modules instance already exists.");

      modules = Collections.synchronizedList(new LinkedList<>());
      modulesInstance = this;
   }

   @SuppressWarnings("unchecked")
   public static <E extends CoreModule> E getModule(Class<E> moduleClass) {

      if(moduleClass == null)
         throw new NullPointerException("Module class is null.");

      return (E) getModulesInstance().getModules().stream()
            .filter(module -> module.getClass().isAssignableFrom(moduleClass))
            .findAny().orElse(null);
   }

   public static void unregisterModule(Class<? extends CoreModule> moduleClass) {

      if(moduleClass == null)
         throw new NullPointerException("Module class is null.");

      CoreModule coreModule = getModulesInstance().getModules().stream()
            .filter(module -> module.getClass().isAssignableFrom(moduleClass))
            .findAny().orElse(null);

      if(coreModule != null) {
         coreModule.onDisabling();
         getModulesInstance().getModules().remove(coreModule);
         logger.info(coreModule.toString() + " was disabled. Functions that use this module may stop working.");
      }
   }

   public static void register(Class<? extends CoreModule> moduleClass) {
      if(getModulesInstance().getModules().stream()
            .noneMatch(module -> module.getClass().isAssignableFrom(moduleClass))) {
         try {
            CoreModule coreModule = moduleClass.getConstructor().newInstance();
            if(getModulesInstance().getModules().add(coreModule)) {
               coreModule.onRegistered();
            }
            return;
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      throw new IllegalCallerException(moduleClass.getTypeName() + " already registered.");
   }
   
   @SuppressWarnings("unchecked")
   @SafeVarargs
   public static void registerAll(Class<?>... moduleClasses) {
      for(Class<?> moduleClass : moduleClasses) {

         if(!CoreModule.class.isAssignableFrom(moduleClass))
            continue;
         
         register((Class<? extends CoreModule>) moduleClass);
      }
   }

   public static void setup() { 
      try {
         new Modules();
      } catch (InstanceAlreadyExistsException e) {
         logger.info(e.getLocalizedMessage());
      } 
   }

   private static Modules getModulesInstance() {
      return modulesInstance;
   }

   public List<CoreModule> getModules() {
      return modules;
   }

}
