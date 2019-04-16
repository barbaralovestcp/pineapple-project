package org.pineapple.stateMachine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.pineapple.CommandPOP3;
import org.pineapple.ICommand;
import org.pineapple.stateMachine.Exception.InvalidCommandException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

public abstract class AbstractInputStateMachine<T extends ICommand> implements IInputStateMachine {
	
	private T command;
	private String[] arguments;
	
	public AbstractInputStateMachine(@NotNull String request, @NotNull Class<T> clazz) {
		String name = clazz.getSimpleName();
		if (name.matches("Command[a-zA-Z0-9]+"))
			name = name.replace("Command", "");

		if(request.split("\\s+")[0].toUpperCase().contains("FROM")){
			this.arguments = new String[]{"DATA", request};
		}else{
			this.arguments = request.split("\\s+");
		}
		
		try {
			Method valueOf = clazz.getMethod("valueOf", String.class);
			this.command = clazz.cast(valueOf.invoke(null, arguments[0].toUpperCase()));
		}
		catch (IllegalArgumentException err) {
			throw new InvalidCommandException("Invalid " + name + " command " + "\'" + arguments[0] + "\'", err);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	protected static <T> boolean isValidRequest(@NotNull String request, @NotNull Class<T> clazz) {
		String[] args = request.split("\\s+");
		try {
			Method valueOf = clazz.getMethod("valueOf", String.class);
			valueOf.invoke(null, args[0].toUpperCase());
			return true;
		}
		catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			System.out.println("Invalid request: \"" + request + "\"");
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return false;
	}
	
	//region Static Method
	@Contract("_ -> fail")
	public static boolean isValidRequest(@NotNull String request) {
		throw new NotImplementedException();
	}
	//endregion
	
	//region Getter Setter
	public T getCommand() {
		return command;
	}
	
	public String[] getArguments() {
		return arguments;
	}
	//endregion
}
