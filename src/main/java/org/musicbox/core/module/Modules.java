package org.musicbox.core.module;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Modules {

   private static Logger logger = LoggerFactory.getLogger(Modules.class);
   private static Modules modulesInstance;

   private Map<Class<? extends CoreModule>, CoreModule> modules;

   private Modules() throws InstanceAlreadyExistsException {
      if(modulesInstance != null)
         throw new InstanceAlreadyExistsException("Modules instance already exists.");

      modules = new LinkedHashMap<>();
      modulesInstance = this;
   }

   @SuppressWarnings("unchecked")
   public static <E extends CoreModule> E getModule(Class<E> moduleClass) {
      if(moduleClass == null)
         throw new NullPointerException("Module class is null.");

      return (E) getModulesInstance().getModules().get(moduleClass);
   }

   public static void register(Class<? extends CoreModule> moduleClass) {
      if(moduleClass == null)
         throw new NullPointerException("Module class is null.");

      try {
         CoreModule module = moduleClass.getConstructor().newInstance();
         getModulesInstance().getModules().putIfAbsent(moduleClass, module);
         module.onEnabled();
         
         logger.info(module.toString() + " registered.");  
      } catch (Throwable e) {
         logger.error(e.getLocalizedMessage());
         System.exit(-1);
      }
   }

   public static void unregister(Class<? extends CoreModule> moduleClass) {
      if(moduleClass == null)
         throw new NullPointerException("Module class is null.");

      getModulesInstance().getModules().keySet().stream()
      .filter(moduleClass::isAssignableFrom).findAny().ifPresent(module -> {
         getModulesInstance().getModules().remove(module);
      });
   }

   public static boolean hasModule(Class<? extends CoreModule> moduleClass) {
      return getModulesInstance().getModules().keySet().stream()
            .filter(moduleClass::isAssignableFrom).findAny().isPresent();
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

   public Map<Class<? extends CoreModule>, CoreModule> getModules() {
      return modules;
   }

}
