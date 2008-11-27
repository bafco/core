/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.webbeans.introspector.jlr;

import java.lang.reflect.Member;

import javax.webbeans.BindingType;

import org.jboss.webbeans.ManagerImpl;
import org.jboss.webbeans.util.Reflections;

/**
 * Represents an abstract annotated memeber (field, method or constructor)
 * 
 * @author Pete Muir
 * 
 * @param <T>
 * @param <S>
 */
public abstract class AbstractAnnotatedMember<T, S extends Member> extends AbstractAnnotatedItem<T, S>
{
   // The name of the member
   private String name;

   /**
    * Constructor
    * 
    * @param annotationMap The annotation map
    */
   public AbstractAnnotatedMember(AnnotationMap annotationMap)
   {
      super(annotationMap);
   }

   /**
    * Indicates if the member is static (through the delegate)
    * 
    * @return True if static, false otherwise
    */
   public boolean isStatic()
   {
      return Reflections.isStatic(getDelegate());
   }

   /**
    * Indicates if the member if final (through the delegate)
    * 
    * @return True if final, false otherwise
    */
   public boolean isFinal()
   {
      return Reflections.isFinal(getDelegate());
   }

   /**
    * Gets the current value of the member
    * 
    * @param manager The Web Beans manager
    * @return The current value
    */
   public T getValue(ManagerImpl manager)
   {
      return manager.getInstanceByType(getType(), getMetaAnnotationsAsArray(BindingType.class));
   }

   /**
    * Gets the name of the member
    * 
    * @returns The name (or the name of the delegate if none is defined)
    */
   public String getName()
   {
      if (name == null)
      {
         name = getDelegate().getName();
      }
      return name;
   }

}
