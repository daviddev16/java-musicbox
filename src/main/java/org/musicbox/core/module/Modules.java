package org.musicbox.core.module;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Modules {

   private static Logger logger = LoggerFactory.getLogger(Modules.class);
   private static Modules modulesInstance;

   private Set<CoreModule> modules;

   private Modules() throws InstanceAlreadyExistsException {

      if(modulesInstance != null)
         throw new InstanceAlreadyExistsException("Modules instance already exists.");

      modules = Collections.synchronizedSet(new HashSet<>());
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

   public static boolean hasModule(Class<? extends CoreModule> moduleClass) {
      return getModulesInstance().getModules().stream()
            .anyMatch(module -> module.getClass().isAssignableFrom(moduleClass));
   }

   public static void register(Class<? extends CoreModule> moduleClass) {
      if(getModulesInstance().getModules().stream()
            .noneMatch(module -> module.getClass().isAssignableFrom(moduleClass))) {
         try {
            CoreModule coreModule = moduleClass.getConstructor().newInstance();
            if(getModulesInstance().getModules().add(coreModule)) {
               coreModule.onRegistered();
               logger.info(coreModule.toString() + " registered.");
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

   public Set<CoreModule> getModules() {
      return modules;
   }

}
