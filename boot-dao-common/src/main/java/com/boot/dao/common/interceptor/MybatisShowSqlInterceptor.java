package com.boot.dao.common.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})})
public class MybatisShowSqlInterceptor implements Interceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected DateFormat formatter =
            DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);

    public Object intercept(Invocation invocation) throws Throwable {

        if (!logger.isInfoEnabled()) {
            return invocation.proceed();
        }

        StringBuilder builder = new StringBuilder("");
        try {

            Object parameter;
            if (invocation.getArgs() != null && invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                Configuration configuration = mappedStatement.getConfiguration();
                String sqlId = mappedStatement.getId();
                String sql = getSql(configuration, boundSql, sqlId);
                builder.append(sql == null ? "" : sql);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        long start = System.currentTimeMillis();
        Object returnValue = invocation.proceed();
        long end = System.currentTimeMillis();

        builder.append(", return value: ").append(returnValue);
        builder.append(", cost: ").append(end - start).append("ms");
        logger.info(builder.toString());
        return returnValue;
    }

    protected String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        String sql = showSql(configuration, boundSql);
        StringBuilder builder = new StringBuilder(100);
        builder.append(sqlId).append(", SQL: ").append(sql);
        return builder.toString();
    }

    protected String getParameterValue(Object obj) {
        StringBuilder sb = new StringBuilder();
        if (obj instanceof String) {
            sb.append("'");
            sb.append(obj.toString());
            sb.append("'");
        } else if (obj instanceof Date) {
            sb.append("'");
            sb.append(formatter.format((Date) obj));
            sb.append("'");
        } else {
            if (obj != null) {
                sb.append(obj.toString());
            }

        }
        return sb.toString().replace("?", "%3f").replace("$", "%24");
    }

    protected String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql.replace("%3f", "?").replace("%24", "$");
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }
}
