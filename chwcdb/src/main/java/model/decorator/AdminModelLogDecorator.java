package model.decorator;

import java.util.List;
import java.util.Optional;

import appexception.CHWCDBException;
import entity.*;
import logger.ILogger;
import model.IModel;

public class AdminModelLogDecorator extends IModel
{
    private IModel model;
    private ILogger logger;

    public AdminModelLogDecorator(
        final IModel model,
        final ILogger logger
    )
    {
        this.model = model;
        this.logger = logger;
    }
}
