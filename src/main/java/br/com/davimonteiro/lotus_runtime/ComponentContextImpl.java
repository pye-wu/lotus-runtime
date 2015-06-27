/**
 * The MIT License
 * Copyright (c) 2015 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.davimonteiro.lotus_runtime;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComponentContextImpl implements ComponentContext {
	
	private List<Component> components;
	
	public ComponentContextImpl() {
		this.components = new CopyOnWriteArrayList<Component>();
	}
		
	@Override
	public void start(ComponentContext context) throws Exception {
		for (Component component : components) {
			log.info("Starting the component: " + component.getClass());
			component.start(this);
		}
	}

	@Override
	public void stop(ComponentContext context) throws Exception {
		for (Component component : components) {
			component.stop(this);
			log.info("Stopping the component: " + component.getClass());
			uninstallComponent(component);
		}
	}
	
	// TODO Refactor this method
	// http://stackoverflow.com/questions/28921833/why-is-this-an-unchecked-cast-and-how-to-fix-it
	// http://stackoverflow.com/questions/496928/what-is-the-difference-between-instanceof-and-class-isassignablefrom
	// http://www.ralfebert.de/archive/java/isassignablefrom/
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<?> clazz) {
		T result = null;
		
		for (Component c : components) {
			if (clazz.isAssignableFrom(c.getClass())) {
				result = (T) c;
				break;
			}
		}
		
		return result;
	}
	
	// TODO Fazer a modificacao de segunraca aqui
	public Component get(Component component) {
		Component result = null;
		
		for (Component c : components) {
			if (component.getClass().isAssignableFrom(c.getClass())) {
				result = c;
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public void installComponent(Component component) throws Exception {
		log.info("Installing the component: " + component.getClass());
		components.add(component);
	}

	@Override
	public void uninstallComponent(Component component) throws Exception {
		log.info("Uninstalling the component: " + component.getClass());
		components.remove(component);
	}

}
