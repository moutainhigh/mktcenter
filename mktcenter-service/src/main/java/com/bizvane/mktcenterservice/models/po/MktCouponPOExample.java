package com.bizvane.mktcenterservice.models.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MktCouponPOExample implements Serializable {
    /**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	protected String orderByClause;
	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	protected boolean distinct;
	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	protected List<Criteria> oredCriteria;
	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public MktCouponPOExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * 只读. t_mkt_coupon
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	protected abstract static class GeneratedCriteria implements Serializable {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andMktCouponIdIsNull() {
			addCriterion("mkt_coupon_id is null");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdIsNotNull() {
			addCriterion("mkt_coupon_id is not null");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdEqualTo(Long value) {
			addCriterion("mkt_coupon_id =", value, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdNotEqualTo(Long value) {
			addCriterion("mkt_coupon_id <>", value, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdGreaterThan(Long value) {
			addCriterion("mkt_coupon_id >", value, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdGreaterThanOrEqualTo(Long value) {
			addCriterion("mkt_coupon_id >=", value, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdLessThan(Long value) {
			addCriterion("mkt_coupon_id <", value, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdLessThanOrEqualTo(Long value) {
			addCriterion("mkt_coupon_id <=", value, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdIn(List<Long> values) {
			addCriterion("mkt_coupon_id in", values, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdNotIn(List<Long> values) {
			addCriterion("mkt_coupon_id not in", values, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdBetween(Long value1, Long value2) {
			addCriterion("mkt_coupon_id between", value1, value2, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andMktCouponIdNotBetween(Long value1, Long value2) {
			addCriterion("mkt_coupon_id not between", value1, value2, "mktCouponId");
			return (Criteria) this;
		}

		public Criteria andBizTypeIsNull() {
			addCriterion("biz_type is null");
			return (Criteria) this;
		}

		public Criteria andBizTypeIsNotNull() {
			addCriterion("biz_type is not null");
			return (Criteria) this;
		}

		public Criteria andBizTypeEqualTo(Integer value) {
			addCriterion("biz_type =", value, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeNotEqualTo(Integer value) {
			addCriterion("biz_type <>", value, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeGreaterThan(Integer value) {
			addCriterion("biz_type >", value, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeGreaterThanOrEqualTo(Integer value) {
			addCriterion("biz_type >=", value, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeLessThan(Integer value) {
			addCriterion("biz_type <", value, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeLessThanOrEqualTo(Integer value) {
			addCriterion("biz_type <=", value, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeIn(List<Integer> values) {
			addCriterion("biz_type in", values, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeNotIn(List<Integer> values) {
			addCriterion("biz_type not in", values, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeBetween(Integer value1, Integer value2) {
			addCriterion("biz_type between", value1, value2, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizTypeNotBetween(Integer value1, Integer value2) {
			addCriterion("biz_type not between", value1, value2, "bizType");
			return (Criteria) this;
		}

		public Criteria andBizIdIsNull() {
			addCriterion("biz_id is null");
			return (Criteria) this;
		}

		public Criteria andBizIdIsNotNull() {
			addCriterion("biz_id is not null");
			return (Criteria) this;
		}

		public Criteria andBizIdEqualTo(Long value) {
			addCriterion("biz_id =", value, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdNotEqualTo(Long value) {
			addCriterion("biz_id <>", value, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdGreaterThan(Long value) {
			addCriterion("biz_id >", value, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdGreaterThanOrEqualTo(Long value) {
			addCriterion("biz_id >=", value, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdLessThan(Long value) {
			addCriterion("biz_id <", value, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdLessThanOrEqualTo(Long value) {
			addCriterion("biz_id <=", value, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdIn(List<Long> values) {
			addCriterion("biz_id in", values, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdNotIn(List<Long> values) {
			addCriterion("biz_id not in", values, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdBetween(Long value1, Long value2) {
			addCriterion("biz_id between", value1, value2, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizIdNotBetween(Long value1, Long value2) {
			addCriterion("biz_id not between", value1, value2, "bizId");
			return (Criteria) this;
		}

		public Criteria andBizCodeIsNull() {
			addCriterion("biz_code is null");
			return (Criteria) this;
		}

		public Criteria andBizCodeIsNotNull() {
			addCriterion("biz_code is not null");
			return (Criteria) this;
		}

		public Criteria andBizCodeEqualTo(String value) {
			addCriterion("biz_code =", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeNotEqualTo(String value) {
			addCriterion("biz_code <>", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeGreaterThan(String value) {
			addCriterion("biz_code >", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeGreaterThanOrEqualTo(String value) {
			addCriterion("biz_code >=", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeLessThan(String value) {
			addCriterion("biz_code <", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeLessThanOrEqualTo(String value) {
			addCriterion("biz_code <=", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeLike(String value) {
			addCriterion("biz_code like", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeNotLike(String value) {
			addCriterion("biz_code not like", value, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeIn(List<String> values) {
			addCriterion("biz_code in", values, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeNotIn(List<String> values) {
			addCriterion("biz_code not in", values, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeBetween(String value1, String value2) {
			addCriterion("biz_code between", value1, value2, "bizCode");
			return (Criteria) this;
		}

		public Criteria andBizCodeNotBetween(String value1, String value2) {
			addCriterion("biz_code not between", value1, value2, "bizCode");
			return (Criteria) this;
		}

		public Criteria andCouponIdIsNull() {
			addCriterion("coupon_id is null");
			return (Criteria) this;
		}

		public Criteria andCouponIdIsNotNull() {
			addCriterion("coupon_id is not null");
			return (Criteria) this;
		}

		public Criteria andCouponIdEqualTo(Long value) {
			addCriterion("coupon_id =", value, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdNotEqualTo(Long value) {
			addCriterion("coupon_id <>", value, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdGreaterThan(Long value) {
			addCriterion("coupon_id >", value, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdGreaterThanOrEqualTo(Long value) {
			addCriterion("coupon_id >=", value, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdLessThan(Long value) {
			addCriterion("coupon_id <", value, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdLessThanOrEqualTo(Long value) {
			addCriterion("coupon_id <=", value, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdIn(List<Long> values) {
			addCriterion("coupon_id in", values, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdNotIn(List<Long> values) {
			addCriterion("coupon_id not in", values, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdBetween(Long value1, Long value2) {
			addCriterion("coupon_id between", value1, value2, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponIdNotBetween(Long value1, Long value2) {
			addCriterion("coupon_id not between", value1, value2, "couponId");
			return (Criteria) this;
		}

		public Criteria andCouponCodeIsNull() {
			addCriterion("coupon_code is null");
			return (Criteria) this;
		}

		public Criteria andCouponCodeIsNotNull() {
			addCriterion("coupon_code is not null");
			return (Criteria) this;
		}

		public Criteria andCouponCodeEqualTo(String value) {
			addCriterion("coupon_code =", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeNotEqualTo(String value) {
			addCriterion("coupon_code <>", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeGreaterThan(String value) {
			addCriterion("coupon_code >", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeGreaterThanOrEqualTo(String value) {
			addCriterion("coupon_code >=", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeLessThan(String value) {
			addCriterion("coupon_code <", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeLessThanOrEqualTo(String value) {
			addCriterion("coupon_code <=", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeLike(String value) {
			addCriterion("coupon_code like", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeNotLike(String value) {
			addCriterion("coupon_code not like", value, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeIn(List<String> values) {
			addCriterion("coupon_code in", values, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeNotIn(List<String> values) {
			addCriterion("coupon_code not in", values, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeBetween(String value1, String value2) {
			addCriterion("coupon_code between", value1, value2, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponCodeNotBetween(String value1, String value2) {
			addCriterion("coupon_code not between", value1, value2, "couponCode");
			return (Criteria) this;
		}

		public Criteria andCouponNameIsNull() {
			addCriterion("coupon_name is null");
			return (Criteria) this;
		}

		public Criteria andCouponNameIsNotNull() {
			addCriterion("coupon_name is not null");
			return (Criteria) this;
		}

		public Criteria andCouponNameEqualTo(String value) {
			addCriterion("coupon_name =", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameNotEqualTo(String value) {
			addCriterion("coupon_name <>", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameGreaterThan(String value) {
			addCriterion("coupon_name >", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameGreaterThanOrEqualTo(String value) {
			addCriterion("coupon_name >=", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameLessThan(String value) {
			addCriterion("coupon_name <", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameLessThanOrEqualTo(String value) {
			addCriterion("coupon_name <=", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameLike(String value) {
			addCriterion("coupon_name like", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameNotLike(String value) {
			addCriterion("coupon_name not like", value, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameIn(List<String> values) {
			addCriterion("coupon_name in", values, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameNotIn(List<String> values) {
			addCriterion("coupon_name not in", values, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameBetween(String value1, String value2) {
			addCriterion("coupon_name between", value1, value2, "couponName");
			return (Criteria) this;
		}

		public Criteria andCouponNameNotBetween(String value1, String value2) {
			addCriterion("coupon_name not between", value1, value2, "couponName");
			return (Criteria) this;
		}

		public Criteria andRemarkIsNull() {
			addCriterion("remark is null");
			return (Criteria) this;
		}

		public Criteria andRemarkIsNotNull() {
			addCriterion("remark is not null");
			return (Criteria) this;
		}

		public Criteria andRemarkEqualTo(String value) {
			addCriterion("remark =", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotEqualTo(String value) {
			addCriterion("remark <>", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkGreaterThan(String value) {
			addCriterion("remark >", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkGreaterThanOrEqualTo(String value) {
			addCriterion("remark >=", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkLessThan(String value) {
			addCriterion("remark <", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkLessThanOrEqualTo(String value) {
			addCriterion("remark <=", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkLike(String value) {
			addCriterion("remark like", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotLike(String value) {
			addCriterion("remark not like", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkIn(List<String> values) {
			addCriterion("remark in", values, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotIn(List<String> values) {
			addCriterion("remark not in", values, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkBetween(String value1, String value2) {
			addCriterion("remark between", value1, value2, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotBetween(String value1, String value2) {
			addCriterion("remark not between", value1, value2, "remark");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdIsNull() {
			addCriterion("create_user_id is null");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdIsNotNull() {
			addCriterion("create_user_id is not null");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdEqualTo(Long value) {
			addCriterion("create_user_id =", value, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdNotEqualTo(Long value) {
			addCriterion("create_user_id <>", value, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdGreaterThan(Long value) {
			addCriterion("create_user_id >", value, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdGreaterThanOrEqualTo(Long value) {
			addCriterion("create_user_id >=", value, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdLessThan(Long value) {
			addCriterion("create_user_id <", value, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdLessThanOrEqualTo(Long value) {
			addCriterion("create_user_id <=", value, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdIn(List<Long> values) {
			addCriterion("create_user_id in", values, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdNotIn(List<Long> values) {
			addCriterion("create_user_id not in", values, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdBetween(Long value1, Long value2) {
			addCriterion("create_user_id between", value1, value2, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserIdNotBetween(Long value1, Long value2) {
			addCriterion("create_user_id not between", value1, value2, "createUserId");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameIsNull() {
			addCriterion("create_user_name is null");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameIsNotNull() {
			addCriterion("create_user_name is not null");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameEqualTo(String value) {
			addCriterion("create_user_name =", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameNotEqualTo(String value) {
			addCriterion("create_user_name <>", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameGreaterThan(String value) {
			addCriterion("create_user_name >", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameGreaterThanOrEqualTo(String value) {
			addCriterion("create_user_name >=", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameLessThan(String value) {
			addCriterion("create_user_name <", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameLessThanOrEqualTo(String value) {
			addCriterion("create_user_name <=", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameLike(String value) {
			addCriterion("create_user_name like", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameNotLike(String value) {
			addCriterion("create_user_name not like", value, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameIn(List<String> values) {
			addCriterion("create_user_name in", values, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameNotIn(List<String> values) {
			addCriterion("create_user_name not in", values, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameBetween(String value1, String value2) {
			addCriterion("create_user_name between", value1, value2, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateUserNameNotBetween(String value1, String value2) {
			addCriterion("create_user_name not between", value1, value2, "createUserName");
			return (Criteria) this;
		}

		public Criteria andCreateDateIsNull() {
			addCriterion("create_date is null");
			return (Criteria) this;
		}

		public Criteria andCreateDateIsNotNull() {
			addCriterion("create_date is not null");
			return (Criteria) this;
		}

		public Criteria andCreateDateEqualTo(Date value) {
			addCriterion("create_date =", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateNotEqualTo(Date value) {
			addCriterion("create_date <>", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateGreaterThan(Date value) {
			addCriterion("create_date >", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
			addCriterion("create_date >=", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateLessThan(Date value) {
			addCriterion("create_date <", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateLessThanOrEqualTo(Date value) {
			addCriterion("create_date <=", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateIn(List<Date> values) {
			addCriterion("create_date in", values, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateNotIn(List<Date> values) {
			addCriterion("create_date not in", values, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateBetween(Date value1, Date value2) {
			addCriterion("create_date between", value1, value2, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateNotBetween(Date value1, Date value2) {
			addCriterion("create_date not between", value1, value2, "createDate");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdIsNull() {
			addCriterion("modified_user_id is null");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdIsNotNull() {
			addCriterion("modified_user_id is not null");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdEqualTo(Long value) {
			addCriterion("modified_user_id =", value, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdNotEqualTo(Long value) {
			addCriterion("modified_user_id <>", value, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdGreaterThan(Long value) {
			addCriterion("modified_user_id >", value, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdGreaterThanOrEqualTo(Long value) {
			addCriterion("modified_user_id >=", value, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdLessThan(Long value) {
			addCriterion("modified_user_id <", value, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdLessThanOrEqualTo(Long value) {
			addCriterion("modified_user_id <=", value, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdIn(List<Long> values) {
			addCriterion("modified_user_id in", values, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdNotIn(List<Long> values) {
			addCriterion("modified_user_id not in", values, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdBetween(Long value1, Long value2) {
			addCriterion("modified_user_id between", value1, value2, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIdNotBetween(Long value1, Long value2) {
			addCriterion("modified_user_id not between", value1, value2, "modifiedUserId");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameIsNull() {
			addCriterion("modified_user_name is null");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameIsNotNull() {
			addCriterion("modified_user_name is not null");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameEqualTo(String value) {
			addCriterion("modified_user_name =", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameNotEqualTo(String value) {
			addCriterion("modified_user_name <>", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameGreaterThan(String value) {
			addCriterion("modified_user_name >", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameGreaterThanOrEqualTo(String value) {
			addCriterion("modified_user_name >=", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameLessThan(String value) {
			addCriterion("modified_user_name <", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameLessThanOrEqualTo(String value) {
			addCriterion("modified_user_name <=", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameLike(String value) {
			addCriterion("modified_user_name like", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameNotLike(String value) {
			addCriterion("modified_user_name not like", value, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameIn(List<String> values) {
			addCriterion("modified_user_name in", values, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameNotIn(List<String> values) {
			addCriterion("modified_user_name not in", values, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameBetween(String value1, String value2) {
			addCriterion("modified_user_name between", value1, value2, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNameNotBetween(String value1, String value2) {
			addCriterion("modified_user_name not between", value1, value2, "modifiedUserName");
			return (Criteria) this;
		}

		public Criteria andModifiedDateIsNull() {
			addCriterion("modified_date is null");
			return (Criteria) this;
		}

		public Criteria andModifiedDateIsNotNull() {
			addCriterion("modified_date is not null");
			return (Criteria) this;
		}

		public Criteria andModifiedDateEqualTo(Date value) {
			addCriterion("modified_date =", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateNotEqualTo(Date value) {
			addCriterion("modified_date <>", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateGreaterThan(Date value) {
			addCriterion("modified_date >", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateGreaterThanOrEqualTo(Date value) {
			addCriterion("modified_date >=", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateLessThan(Date value) {
			addCriterion("modified_date <", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateLessThanOrEqualTo(Date value) {
			addCriterion("modified_date <=", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateIn(List<Date> values) {
			addCriterion("modified_date in", values, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateNotIn(List<Date> values) {
			addCriterion("modified_date not in", values, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateBetween(Date value1, Date value2) {
			addCriterion("modified_date between", value1, value2, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateNotBetween(Date value1, Date value2) {
			addCriterion("modified_date not between", value1, value2, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andValidIsNull() {
			addCriterion("valid is null");
			return (Criteria) this;
		}

		public Criteria andValidIsNotNull() {
			addCriterion("valid is not null");
			return (Criteria) this;
		}

		public Criteria andValidEqualTo(Boolean value) {
			addCriterion("valid =", value, "valid");
			return (Criteria) this;
		}

		public Criteria andValidNotEqualTo(Boolean value) {
			addCriterion("valid <>", value, "valid");
			return (Criteria) this;
		}

		public Criteria andValidGreaterThan(Boolean value) {
			addCriterion("valid >", value, "valid");
			return (Criteria) this;
		}

		public Criteria andValidGreaterThanOrEqualTo(Boolean value) {
			addCriterion("valid >=", value, "valid");
			return (Criteria) this;
		}

		public Criteria andValidLessThan(Boolean value) {
			addCriterion("valid <", value, "valid");
			return (Criteria) this;
		}

		public Criteria andValidLessThanOrEqualTo(Boolean value) {
			addCriterion("valid <=", value, "valid");
			return (Criteria) this;
		}

		public Criteria andValidIn(List<Boolean> values) {
			addCriterion("valid in", values, "valid");
			return (Criteria) this;
		}

		public Criteria andValidNotIn(List<Boolean> values) {
			addCriterion("valid not in", values, "valid");
			return (Criteria) this;
		}

		public Criteria andValidBetween(Boolean value1, Boolean value2) {
			addCriterion("valid between", value1, value2, "valid");
			return (Criteria) this;
		}

		public Criteria andValidNotBetween(Boolean value1, Boolean value2) {
			addCriterion("valid not between", value1, value2, "valid");
			return (Criteria) this;
		}
	}

	/**
	 * 只读. t_mkt_coupon
	 * @mbg.generated  2018-07-27 14:35:28
	 */
	public static class Criterion implements Serializable {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**9
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_mkt_coupon
     *
     * @mbg.generated do_not_delete_during_merge 2018-07-14 13:25:51
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }
}