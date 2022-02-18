package com.kmetha.cloudstorage.core.action;

import java.io.Serializable;

public interface ActionModel extends Serializable {

    CommandType getType();
}
